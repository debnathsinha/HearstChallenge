package com.cleveralgorithms.hearst.nn;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cleveralgorithms.hearst.FileIO;


public abstract class ImpTestNearestNeighbourSales extends DefaultNearestNeighbourSales
{	
	public final static NumberFormat f = NumberFormat.getNumberInstance();
	
	protected Map<String, Map<Integer,Double>> verificationSales = new HashMap<String, Map<Integer,Double>>();
	protected Set<Integer> allTestStores = new HashSet<Integer>();
	
	protected OfflineConfiguration configuration = new OfflineConfiguration(Integer.MAX_VALUE);
	
	protected boolean testMode = true;
	
	protected void postComputation()
	{
		if (testMode) {
			System.out.println(getExperimentResultString());	
		} else {
			// write test sales NN data
			try {
				writeTestSalesData();
			} catch(Exception e) {
				throw new RuntimeException("Fatal Exception: " + e.getMessage(), e);
			}
		}		
	}
	
	
	
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
		String salesQuery = (testMode) ? getTestSalesQuery() : getSubmissionSalesQuery();
		int total = loadSalesDataFromDatabase(testSales, salesQuery);
		System.out.println(">Loaded "+total + " test sales records from the database.");
		
		if (testMode) {
			// copy sales data for verification purposes
			copySalesData(testSales, verificationSales, allTestStores);
		} else {
			verificationSales = null;
			allTestStores = null;
		}
		
		// stores
		String storeQuery = getTestStoresQuery(allTestStores);
		total = loadSoresDataFromDatabase(testStores, storeQuery);
		System.out.println(">Loaded "+total + " test store records from the database.");

	}
	

	
	protected void preRunFinalization()
	{
		if (!testMode) {
			return;
		}
		
		// wipe answers
		clearPredictions();
		
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
		
		if (testMode) {
			// add store_key conditions
			whereConditions.append(" AND store_key in (");
			whereConditions.append(join(storeIds, ","));
			whereConditions.append(")");			
		}
		
		String table = (testMode) ? "store_mo2" : "store_vd2"; // '2' includes store type id 
				
		return "select store_key,"+selectFields+" from "+table+" where "+whereConditions;
	}
	
	protected String getTestSalesQuery()
	{
		// a fixed random sample of mo records
		return "select store_key, title_key, on_year, on_month, sales_total from template_mo2 where on_year = 2009 order by rand(1) limit 20000";
	}
	
	protected String getSubmissionSalesQuery()
	{
		// a fixed random sample of mo records
		return "select store_key, title_key, on_year, on_month, sales_total from template_vd2";
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
	
	protected void writeTestSalesData() throws IOException
	{
		StringBuilder buf = new StringBuilder();
		int count = 0;
		int total = 0;
		
		// process all title/year/months in test
		for(String key : testSales.keySet()) {
			Map<Integer,Double> salesData = testSales.get(key);
			
			// process all stores
			for(Integer storeKey : salesData.keySet()) {
				total++;
				Double sales = salesData.get(storeKey);
				
				if (sales == null || sales.isNaN()) {
					continue;
				}
				
				String [] parts = key.split("-");								
				buf.append(storeKey);
				buf.append(",");
				for (int i = 0; i < parts.length; i++) {
					buf.append(parts[i]);
					buf.append(",");
				}
				buf.append(sales.toString());
				buf.append("\n");
				count++;
			}			
		}
		String filename = getOutputFilename();
		FileIO.writeStringToFile(buf.toString(), new File(filename));
		System.out.println("Successfully wrote "+count+" of "+total+" records to file: " + filename);
	}
	
	protected String getOutputFilename(){
		return "dat/"+getClass().getSimpleName()+"_nn_sales_data.dat";
	}

	public boolean isTestMode() {
		return testMode;
	}
	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}	
}

