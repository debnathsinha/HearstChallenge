package com.cleveralgorithms.hearst.nn.file;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cleveralgorithms.hearst.FileIO;



public class SummarizedAreaNearestNeighbourSales 
{
	public final static String TRAIN_SALES_FILENAME = "experiments/export/export_train.dat";
	public final static String TRAIN_STORES_FILENAME = "experiments/export/export_train_stores.dat";
	
	protected Map<Integer,double[]> trainStores = new HashMap<Integer, double[]>();
	protected Map<String, Map<Integer,Double>> trainSales = new HashMap<String, Map<Integer,Double>>();
	
	public final static String TEST_SALES_FILENAME = "experiments/export/export_test.dat";
	public final static String TEST_STORES_FILENAME = "experiments/export/export_test_stores.dat";
	
	protected Map<Integer,double[]> testStores = new HashMap<Integer, double[]>();
	protected Map<String, Map<Integer,Double>> testSales = new HashMap<String, Map<Integer,Double>>();
	
	public final static String [] STORE_FIELDS = {
		"summarized_area_lvl_statistics_a", "summarized_area_lvl_statistics_b", 
		"summarized_area_lvl_statistics_c", "summarized_area_lvl_statistics_d", 
		"summarized_area_lvl_statistics_e", "summarized_area_lvl_statistics_f",
		"summarized_area_lvl_statistics_g", "summarized_area_lvl_statistics_h"}; 
	
	public final static int NUM_THREADS = 4;
	
	public final static int K = 20;
	
	public final static String OUTPUT_FILENAME = "dat/summarized_nn_sales_data.csv";		
	
	public void compute()
	{
		System.out.println("Loading data...");
		try {
			loadTrainingData();
			loadTestingData();
		} catch(Exception e) {
			throw new RuntimeException("Fatal Exception: " + e.getMessage(), e);
		}
		
		// prepare the thread pool
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();		
		ThreadPoolExecutor pool = new ThreadPoolExecutor(NUM_THREADS, NUM_THREADS, 1, TimeUnit.MINUTES, queue);
		// process all title/year/months in test
		for(String key : testSales.keySet()) {
			// process all stores
			for(Integer storekey : testSales.get(key).keySet()) {
				pool.execute(new NNComputeTask(key, storekey));
			}			
		}
		pool.shutdown();
		System.out.println("> queue is fully loaded and has "+queue.size()+" elements remaining");
		while (!queue.isEmpty()) {
			try {
				while (!pool.awaitTermination(1, TimeUnit.MINUTES)) {
					System.out.println("> waited 1 minute, queue has "+queue.size()+" elements remaining");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("> queue has "+queue.size()+" elements remaining");

		// free some memory
		trainStores = null;
		trainSales = null;
		testStores = null;
		
		// write test sales NN data
		try {
			writeTestSalesData();
		} catch(Exception e) {
			throw new RuntimeException("Fatal Exception: " + e.getMessage(), e);
		}
		
		System.out.println("done");
	}
	
	protected void writeTestSalesData() throws IOException
	{
		StringBuilder buf = new StringBuilder();
		
		// process all title/year/months in test
		for(String key : testSales.keySet()) {
			Map<Integer,Double> salesData = testSales.get(key);
			
			// process all stores
			for(Integer storeKey : salesData.keySet()) {
				String [] parts = key.split("-");
				Double sales = salesData.get(storeKey);				
				buf.append(storeKey);
				buf.append(",");
				for (int i = 0; i < parts.length; i++) {
					buf.append(parts[i]);
					buf.append(",");
				}
				buf.append(sales.toString());
				buf.append("\n");
			}			
		}
		
		FileIO.writeStringToFile(buf.toString(), new File(OUTPUT_FILENAME));
		System.out.println("Successfully wrote file: " + OUTPUT_FILENAME);
	}
	
	protected void calculateSalesForKey(String testSalesKey, Integer testStoreKey) 
	{
		// get all candidate train stores
		Map<Integer,Double> trainSalesData = trainSales.get(testSalesKey);
		Collection<Integer> candidateStoresKeys = trainSalesData.keySet();
		if (candidateStoresKeys.isEmpty()) {
			System.out.println(" skipping [key="+testSalesKey+", store="+testStoreKey+"], no base records");
			return;
		}
		
		// calculate distance to candidate stores
		List<Store> candidateStores = new LinkedList<Store>();
		for(Integer trainStoreKey : candidateStoresKeys) {
			Double distance = calculateDistanceToStore(trainStoreKey, testStoreKey);
			candidateStores.add(new Store(trainStoreKey, distance));
		}		
		// select the best k stores
		Collections.sort(candidateStores);		
		// calculate the average sales
		double sales = 0.0;
		int count = 0;
		for (int i = 0; i < candidateStores.size() && i<K; i++, count++) {
			Integer storeKey = candidateStores.get(i).trainStoreId;
			sales += trainSalesData.get(storeKey).doubleValue();
		}
		// average
		sales /= (double)count;		
		// assign to test record		
		testSales.get(testSalesKey).put(testStoreKey, new Double(sales));
//		System.out.println("key="+testSalesKey+", store="+testStoreKey+", sales="+sales);
	}
	
	public double calculateDistanceToStore(Integer trainStoreId, Integer testStoreId)
	{
		double [] v1 = trainStores.get(trainStoreId);
		double [] v2 = testStores.get(testStoreId);
		double sum = 0;
		for (int i = 0; i < v2.length; i++) {
			double diff = v1[i]-v2[i];
			sum += diff*diff;
		}
		//return Math.sqrt(sum);
		return sum;
	}
	
	public class Store implements Comparable<Store>
	{
		public final Double distance;
		public final Integer trainStoreId;
		
		public Store(Integer trainStoreId, Double distance) {
			this.trainStoreId = trainStoreId;
			this.distance = distance;
		}

		public int compareTo(Store other) { 
			return this.distance.compareTo(other.distance); 
		}
	}
	
	protected void loadTestingData() throws IOException
	{
		// sales
		loadSalesData(TEST_SALES_FILENAME, false, testSales);
		System.out.println(">Loaded "+testSales.size() + " test sales records from: " + TEST_SALES_FILENAME);
		loadStoreData(TEST_STORES_FILENAME, testStores);
		System.out.println(">Loaded "+testStores.size() + " test store records from: " + TEST_STORES_FILENAME);
	}
	
	protected void loadTrainingData() throws IOException
	{
		// sales
		loadSalesData(TRAIN_SALES_FILENAME, true, trainSales);
		System.out.println(">Loaded "+trainSales.size() + " training sales record from: "+ TRAIN_SALES_FILENAME);
		loadStoreData(TRAIN_STORES_FILENAME, trainStores);
		System.out.println(">Loaded "+trainStores.size() + " training store records from: " + TRAIN_STORES_FILENAME);
	}
	
	protected void loadStoreData(String filename, Map<Integer, double[]> stores) throws IOException
	{
		String storeData = FileIO.fastLoadFileAsString(new File(filename));
		String [] lines = storeData.split("\n");
		for(String line : lines)
		{
			String [] lineParts = line.split(",");
			if(lineParts.length != 1+STORE_FIELDS.length)
			{
				System.out.println(" skipping line " + line);
			}
			Integer key = Integer.parseInt(lineParts[0]);
			double [] value = new double[STORE_FIELDS.length];
			for (int i = 1; i < lineParts.length; i++) {
				value[i-1] = Double.parseDouble(lineParts[i]);
			}
			stores.put(key, value);
		}
	}
	
	protected void loadSalesData(
			String filename, 
			boolean includesSales, 
			Map<String, Map<Integer,Double>> sales) 
	throws IOException
	{
		String salesData = FileIO.fastLoadFileAsString(new File(filename));
		String [] lines = salesData.split("\n");
		for(String line : lines)
		{			
			String [] lineParts = line.split(",");
			if((includesSales && lineParts.length != 5) || (!includesSales && lineParts.length != 4)) {
				throw new IOException("Bad number of parts in line: " + line);				
			}
			// map by title/year/month
			String key = toSalesKey(lineParts[1], lineParts[2], lineParts[3]);
			Map<Integer,Double> value = sales.get(key);
			if (value == null) {
				value = new HashMap<Integer,Double>();
				sales.put(key, value);
			}
			// map store to sales info
			Integer storeKey = Integer.parseInt(lineParts[0]);
			Double salesValue = (includesSales) ? Double.parseDouble(lineParts[4]) : Double.NaN;
			value.put(storeKey, salesValue);
		}
	}
	
	public final static String toSalesKey(String titleKey, String yearKey, String monthKey)
	{
		return titleKey+"-"+yearKey+"-"+monthKey;
	}
	
	
	
	public static void main(String[] args) {
		new SummarizedAreaNearestNeighbourSales().compute();
	}

	public class NNComputeTask implements Runnable
	{
		private final String testKey;
		private final Integer storeKey;
		
		public NNComputeTask(String testKey, Integer storeKey) {
			this.testKey = testKey;
			this.storeKey = storeKey;
		}

		@Override
		public void run() {
			calculateSalesForKey(testKey, storeKey);			
		}		
	}
}
