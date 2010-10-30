package com.cleveralgorithms.hearst.nn;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.cleveralgorithms.hearst.FileIO;



public class NearestNeighbourSales 
{
	public final static String TRAIN_SALES_FILENAME = "experiments/export/export_train.dat";
	public final static String TRAIN_STORES_FILENAME = "experiments/export/export_train_stores.dat";
	
	protected Map<Integer,double[]> trainStores = new HashMap<Integer, double[]>();
	protected Map<String, SalesRecord> trainSales = new HashMap<String, SalesRecord>();
	
	public final static String TEST_SALES_FILENAME = "experiments/export/export_test.dat";
	public final static String TEST_STORES_FILENAME = "experiments/export/export_test_stores.dat";
	
	protected Map<Integer,double[]> testStores = new HashMap<Integer, double[]>();
	protected Map<String, SalesRecord> testSales = new HashMap<String, SalesRecord>();
	
	public final static String [] STORE_FIELDS = {
		"summarized_area_lvl_statistics_a", "summarized_area_lvl_statistics_b", 
		"summarized_area_lvl_statistics_c", "summarized_area_lvl_statistics_d", 
		"summarized_area_lvl_statistics_e", "summarized_area_lvl_statistics_f",
		"summarized_area_lvl_statistics_g", "summarized_area_lvl_statistics_h"}; 
	
	public final static int NUM_THREADS = 8;
	
	public void compute()
	{
		try {
			loadTrainingData();
			loadTestingData();
		} catch(Exception e) {
			throw new RuntimeException("Fatal Exception: " + e.getMessage(), e);
		}
		
		// mulit-thread the computation
		
		
		System.out.println("done");
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
			Map<String, SalesRecord> sales) 
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
			String key = toSalesKey(lineParts[0], lineParts[1], lineParts[2], lineParts[3]);
			SalesRecord value = (includesSales) ? new SalesRecord(lineParts[0], lineParts[4]) : new SalesRecord(lineParts[0]) ;
			sales.put(key, value);
		}
	}
	
	public final static String toSalesKey(String storekey, String titleKey, String yearKey, String monthKey)
	{
		return storekey+"-"+titleKey+"-"+yearKey+"-"+monthKey;
	}
	
	public static void main(String[] args) {
		new NearestNeighbourSales().compute();
	}
	
	
	public static class SalesRecord
	{		
		public final int storeKey;
		public double sales = Double.NaN;
		
		public SalesRecord(String store, String sales) {
			this(Integer.parseInt(store), Double.parseDouble(sales));
		}
		
		public SalesRecord(String store) {
			this(Integer.parseInt(store), Double.NaN);
		}
		
		public SalesRecord(int store, double sales) {
			this.sales = sales;
			this.storeKey = store;
		}
	}
}
