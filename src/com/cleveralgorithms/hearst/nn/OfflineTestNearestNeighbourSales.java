package com.cleveralgorithms.hearst.nn;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class OfflineTestNearestNeighbourSales extends DefaultNearestNeighbourSales
{	
	protected Map<String, Map<Integer,Double>> verificationSales = new HashMap<String, Map<Integer,Double>>();
	protected Set<Integer> allTestStores = new HashSet<Integer>();
	
	protected OfflineConfiguration configuration = new OfflineConfiguration(Integer.MAX_VALUE);
	
	protected void postComputation()
	{
		System.out.println(getExperimentResultString());
	}
	
	public final static NumberFormat f = NumberFormat.getNumberInstance();
	
	public String getExperimentResultString()
	{
		// calc RMSE for sales data
		double sum = 0;
		int count = 0;
		int total = 0;
		for(String key : testSales.keySet())
		{
			Map<Integer,Double> predictedMap = testSales.get(key);
			Map<Integer,Double> actualMap = verificationSales.get(key);
			
			for(Integer storeKey : predictedMap.keySet())
			{
				total++;
				Double predicted = predictedMap.get(storeKey);
				Double actual = actualMap.get(storeKey);
				
				if (predicted.isNaN()) {
					continue;
				}
				
				double diff = predicted.doubleValue() - actual.doubleValue();
				sum  += diff*diff;
				count++;
			}
		}
		double mse = sum / (double) count;
		double rmse = Math.sqrt(mse);
		double coverage = ((double)count/(double)total)*100.0;
		
		StringBuilder b = new StringBuilder();
		b.append("| ");
		b.append(this.toString());
		b.append(" | ");
		b.append(f.format(coverage) + "%");
		b.append(" | ");
		b.append(f.format(rmse));
		b.append("|");
		
		return b.toString();
	}
	
	protected int getKNeighbours()
	{
		return configuration.k;
	}

	@Override
	protected void loadTestingData() throws Exception {
		// sales
		int total = loadSalesDataFromDatabase(testSales, getTestSalesQuery());
		System.out.println(">Loaded "+total + " test sales records from the database.");
		// copy sales data for verification purposes
		copySalesData(testSales, verificationSales, allTestStores);
		// stores
		total = loadSoresDataFromDatabase(testStores, getTestStoresQuery(allTestStores));
		System.out.println(">Loaded "+total + " test store records from the database.");
	}
	
	public void clearPredictions()
	{
		for(String key : testSales.keySet())
		{
			Map<Integer,Double> testSalesData = testSales.get(key);
			for(Integer storeKey : testSalesData.keySet()) {
				testSalesData.put(storeKey, Double.NaN);
			}
		}
	}
	
	protected void preRunFinalization()
	{
		// wipe answers
		clearPredictions();
		
		// delete all test records from the training data set
		
		// delete test stores in train data
		int deleted = 0;
		for(Integer key : testStores.keySet())
		{
			if(trainStores.containsKey(key)) {
				trainStores.remove(key);
				deleted++;
			}
		}
		
		System.out.println("Deleted "+deleted+" test stores from training store data.");
		
		// delete all test sales data from the train sales data storage
		deleted = 0;
		for(String key : testSales.keySet()) {
			Map<Integer,Double> testSalesData = testSales.get(key);
			Map<Integer,Double> trainSalesData = trainSales.get(key);
			if (trainSalesData == null || trainSalesData.isEmpty()) {
				continue;
			}
			
			for(Integer storeKey : testSalesData.keySet()) {
				if(trainSalesData.containsKey(storeKey)) {
					trainSalesData.remove(storeKey);
					deleted++;
				}
			}
		}
		System.out.println("Deleted "+deleted+" test sales records from training sales data.");
	}
	
	protected void copySalesData(Map<String, Map<Integer,Double>> src, Map<String, Map<Integer,Double>> dst, Set<Integer> allStores)
	{
		for(String key : src.keySet())
		{
			Map<Integer,Double> dstMap = new HashMap<Integer,Double>();
			dst.put(key, dstMap);

			Map<Integer,Double> srcMap = src.get(key);
			for(Integer storeKey : srcMap.keySet())
			{
				// copy sales
				Double value = srcMap.get(storeKey);				
				dstMap.put(storeKey, value);
				// track all stores
				allStores.add(storeKey);
			}
		}
	}

	protected String getTestStoresQuery(Collection<Integer> storeIds)
	{
		// build select fields
		String [] fields = getFields();
		String selectFields = join(fields,",");
		
		// build where fields - protect from nulls
		StringBuilder whereConditions = new StringBuilder();
		for (int i = 0; i < fields.length; i++) {
			whereConditions.append(fields[i]);
			whereConditions.append(" IS NOT NULL");
			if (i!=fields.length-1) {
				whereConditions.append(" AND ");
			}
		}
		// add store_key conditions
		whereConditions.append(" AND store_key in (");
		whereConditions.append(join(storeIds, ","));
		whereConditions.append(")");
				
		return "select store_key,"+selectFields+" from store_mo where "+whereConditions;
	}
	
	protected String getTestSalesQuery()
	{
		// a fixed random sample of mo records
		return "select store_key, title_key, on_year, on_month, sales_total from template_mo where on_year = 2009 order by rand(1) limit 20000";
	}
	
	public void setConfiguration(OfflineConfiguration c)
	{
		this.configuration = c;
	}
	
	public String toString()
	{
		return this.getClass().getSimpleName() + "[k="+configuration.k+"]";
	}
	
	public abstract List<Configuration> getConfigurations();
	
	public static class OfflineConfiguration implements Configuration
	{
		public final int k;
		public OfflineConfiguration(int k) {
			this.k=k;
		}		
	}
}

