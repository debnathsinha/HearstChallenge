package com.cleveralgorithms.hearst.nn;

import java.util.Arrays;
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



public abstract class NearestNeighbourSales 
{
	public final static int NUM_THREADS = 4;
	
	public final static boolean SHOW_SKIPPED = false;
	
	// training records
	protected Map<Integer,double[]> trainStores = new HashMap<Integer, double[]>();
	protected Map<String, Map<Integer,Double>> trainSales = new HashMap<String, Map<Integer,Double>>();
	// test records
	protected Map<Integer,double[]> testStores = new HashMap<Integer, double[]>();
	protected Map<String, Map<Integer,Double>> testSales = new HashMap<String, Map<Integer,Double>>();
	
	private long startTime;
	private long endTime;
	
	public NearestNeighbourSales()
	{	}
	
	public void run() {
		loadData();
		compute();
		postComputation();
		System.out.println("done");
	}
	
	public void loadData() {
		System.out.println("Loading data...");
		try {
			loadTrainingData();
			loadTestingData();
		} catch(Exception e) {
			throw new RuntimeException("Fatal exception loading data: " + e.getMessage(), e);
		}
		
		preRunFinalization();
		
		normalizeVectors();
	}
	
	
	protected void normalizeVectors()
	{
		double [][] minmax = null;
		
		// bounds
		for(double [] v : trainStores.values())
		{
			if (minmax == null) {
				minmax = new double[v.length][];
				for (int i = 0; i < minmax.length; i++) {
					minmax[i] = new double[]{Double.MAX_VALUE, Double.MIN_VALUE};
				}
			}
			for (int i = 0; i < v.length; i++) {
				if (v[i]<minmax[i][0]){minmax[i][0] = v[i];}
				if (v[i]>minmax[i][1]){minmax[i][1] = v[i];}
			}
		}
		for(double [] v : testStores.values())
		{
			for (int i = 0; i < v.length; i++) {
				if (v[i]<minmax[i][0]){minmax[i][0] = v[i];}
				if (v[i]>minmax[i][1]){minmax[i][1] = v[i];}
			}
		}
		
//		for (int i = 0; i < minmax.length; i++) {
//			System.out.println(i+" = " + Arrays.toString(minmax[i]));
//		}
		
		// normalize
		for(double [] v : trainStores.values())
		{
			for (int i = 0; i < v.length; i++) {
				double range = minmax[i][1]-minmax[i][0];
				if (range == 0) {
					v[i] = 0;
				} else {
					v[i] = (v[i]-minmax[i][0]) / range;	
				}
				 
				if(v[i]>1||v[i]<0) {
					throw new RuntimeException("Normalization failed with: " + v[i]);
				}
			}
		}
		for(double [] v : testStores.values())
		{
			for (int i = 0; i < v.length; i++) {
				double range = minmax[i][1]-minmax[i][0];
				if (range == 0) {
					v[i] = 0;
				} else {
					v[i] = (v[i]-minmax[i][0]) / range;	
				}
				 
				if(v[i]>1||v[i]<0) {
					throw new RuntimeException("Normalization failed with: " + v[i]);
				}
			}
		}
	}
	
	
	public void compute()
	{
		startTime = System.currentTimeMillis();
		
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
//		System.out.println("> queue is fully loaded and has "+queue.size()+" elements remaining");
		while (!queue.isEmpty()) {
			try {
				while (!pool.awaitTermination(1, TimeUnit.MINUTES)) {
					System.out.println("> waited 1 minute, queue has "+queue.size()+" elements remaining");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
//		System.out.println("> queue has "+queue.size()+" elements remaining");
		
		endTime = System.currentTimeMillis();		
		long seconds = (endTime-startTime)/1000;
		long minutes = seconds/60;
		long remainingSeconds = seconds%60;		
		System.out.println("Finsihed in "+minutes+" minutes and "+remainingSeconds+" seconds.");
	}
	
	protected void calculateSalesForKey(String testSalesKey, Integer testStoreKey) 
	{
		// get all candidate train stores
		Map<Integer,Double> trainSalesData = trainSales.get(testSalesKey);
		Collection<Integer> candidateStoresKeys = trainSalesData.keySet();
		if (candidateStoresKeys.isEmpty()) {
			if (SHOW_SKIPPED) {
				System.out.println(" skipping [key="+testSalesKey+", store="+testStoreKey+"], no base records");
			}
			return;
		}
		
		// calculate distance to candidate stores
		List<Store> candidateStores = new LinkedList<Store>();
		for(Integer trainStoreKey : candidateStoresKeys) {
			Double distance = calculateDistanceToStore(trainStoreKey, testStoreKey);
			if (distance.isNaN()){
				continue;
			}
			candidateStores.add(new Store(trainStoreKey, distance));
		}		
		if (candidateStores.isEmpty()) {
			if (SHOW_SKIPPED) {
				System.out.println(" skipping [key="+testSalesKey+", store="+testStoreKey+"], no candidates were considered (NaN distance)");
			}
			return;
		}
		
		// predict sales
		double sales = predictSales(testSalesKey, candidateStores);
		// assign to test record		
		testSales.get(testSalesKey).put(testStoreKey, new Double(sales));
	}
	
	protected double predictSales(String testSalesKey, List<Store> candidateStores) {
		// oder stores by distance to store
		Collections.sort(candidateStores);	
		
		// calculate the average sales
		double sales = 0.0;
		int count = 0;
		
		for (int i = 0; i < candidateStores.size() && i<getKNeighbours(); i++, count++) {
			Store candidate = candidateStores.get(i);
			Integer storeKey = candidate.trainStoreId;
			sales += trainSales.get(testSalesKey).get(storeKey).doubleValue();
		}
		// average
		return (sales / (double)count);
	}
	
	protected double calculateDistanceToStore(Integer trainStoreId, Integer testStoreId)
	{
		double [] v1 = trainStores.get(trainStoreId);
		double [] v2 = testStores.get(testStoreId);
		
		if (v1 == null || v1.length==0 || v2 == null || v2.length==0) {
			return Double.NaN;
		}
		
		return euclideanDistance(v1,v2);
	}
	
	public final static double euclideanDistance(double [] v1, double [] v2) {
		// default to euclidean distance
		double sum = 0;
		for (int i = 0; i < v2.length; i++) {
			double diff = v1[i]-v2[i];
			sum += diff*diff;
		}
		//return Math.sqrt(sum);
		return sum;
	}
	
	
	public final static String toSalesKey(String titleKey, String yearKey, String monthKey)
	{
		return titleKey+"-"+yearKey+"-"+monthKey;
	}
	
	protected void configure(Configuration cfg) 
	{
		
	}

	protected void preRunFinalization(){}
	
	protected abstract void postComputation();
	
	protected abstract int getKNeighbours();

	protected abstract void loadTestingData() throws Exception;
	
	protected abstract void loadTrainingData() throws Exception;
	


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
