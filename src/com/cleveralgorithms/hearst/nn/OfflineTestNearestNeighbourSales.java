package com.cleveralgorithms.hearst.nn;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public abstract class OfflineTestNearestNeighbourSales extends DefaultNearestNeighbourSales
{	
	protected Map<String, Map<Integer,Double>> verificationSales = new HashMap<String, Map<Integer,Double>>();

	protected Set<Integer> allTestStores = new HashSet<Integer>();
	
	protected void postComputation()
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
		
		System.out.println("Made predictions for "+count+" of "+total+" records");
		System.out.println("RMSE: " + rmse);
	}
	

	@Override
	protected void loadTestingData() throws Exception {
		// sales
		int total = loadSalesDataFromDatabase(testSales, getTestSalesQuery());
		System.out.println(">Loaded "+total + " test sales records from the database.");
		// copy it
		copySalesData(testSales, verificationSales, allTestStores);
		
		// stores
		total = loadSoresDataFromDatabase(testStores, getTestStoresQuery(allTestStores));
		System.out.println(">Loaded "+total + " test store records from the database.");
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
}

