package com.cleveralgorithms.hearst.nn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cleveralgorithms.hearst.DbIO;

public abstract class DefaultNearestNeighbourSales extends NearestNeighbourSales 
{
	
	@Override
	protected void loadTrainingData() throws Exception
	{
		// sales
		int total = loadSalesDataFromDatabase(trainSales, prepareTrainSalesQuery());
		System.out.println(">Loaded "+total + " TRAIN sales records from the database.");
		
		// stores
		total = loadSoresDataFromDatabase(trainStores, getTrainStoresQuery());
		System.out.println(">Loaded "+total + " TRAIN store records from the database.");
	}
	
	protected String prepareTrainSalesQuery()
	{
		return "select store_key, title_key, on_year, on_month, sales_total from template_mo";
	}
	
	protected String getTrainStoresQuery()
	{
		// build select fields
		String [] fields = getFields();
		String selectFields = join(fields,", ");
		
		// build where fields - protect from nulls
		StringBuilder whereConditions = new StringBuilder();
		for (int i = 0; i < fields.length; i++) {
			whereConditions.append(fields[i]);
			whereConditions.append(" IS NOT NULL");
			if (i!=fields.length-1) {
				whereConditions.append(" AND ");
			}
		}
		
		return "select store_key,"+selectFields+" from store_mo where "+whereConditions.toString();
	}

	
	protected int loadSalesDataFromDatabase(Map<String, Map<Integer,Double>> sales, String query) throws Exception
	{
		int total = 0;
		Connection conn = null;
		ResultSet rs = null;		
		try {
			conn = DbIO.getConnection();
			rs = DbIO.executeReadOnlyQuery(conn, query);			
			while(rs.next())  {
				// get data
				String key = toSalesKey(rs.getInt("title_key"), rs.getInt("on_year"), rs.getInt("on_month"));
				Double salesTotal = rs.getDouble("sales_total");
				Integer storeKey = rs.getInt("store_key");
				// get map for key
				Map<Integer,Double> value = sales.get(key);
				if (value == null) {
					value = new HashMap<Integer,Double>();
					sales.put(key, value);
				}
				// map store to sales info against key
				value.put(storeKey, salesTotal);
				total++;
			} 
		} finally {
			try {if(rs!=null)rs.close();} catch(Exception e) {}
			try {if(conn!=null)conn.close();} catch(Exception e) {}
		}
		return total;
	}
	
	public final static String toSalesKey(int titleKey, int yearKey, int monthKey)
	{
		return toSalesKey(Integer.toString(titleKey),Integer.toString(yearKey),Integer.toString(monthKey));
	}
	
	protected int loadSoresDataFromDatabase(Map<Integer,double[]> storeData, String query) throws Exception
	{
		int total = 0;
		Connection conn = null;
		ResultSet rs = null;		
		try {
			String [] fields = getFields();
			
			conn = DbIO.getConnection();
			rs = DbIO.executeReadOnlyQuery(conn, query);
			while(rs.next())  {
				// get data
				Integer key = rs.getInt("store_key");			
				
				double [] value = new double[fields.length];
				for (int i = 0; i < fields.length; i++) {					
					value[i] = rs.getDouble(fields[i]);
				}
				storeData.put(key, value);
				total++;
			} 
		} finally {
			try {if(rs!=null)rs.close();} catch(Exception e) {}
			try {if(conn!=null)conn.close();} catch(Exception e) {}
		}
		return total;
	}
	
	protected abstract String [] getFields();
	
	
	protected final static String join(Object [] o, String v)
	{
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < o.length; i++) {
			b.append(o[i].toString());
			if(i!=o.length-1) {
				b.append(v);
			}
		}
		return b.toString();
	}
	
	protected final static String join(Collection<? extends Object> c, String v)
	{
		StringBuilder b = new StringBuilder();
		int i = 0;
		for(Object o : c)
		{
			b.append(o.toString());
			if(i!=c.size()-1) {
				b.append(v);
			}
			i++;
		}
		return b.toString();
	}
}
