package com.cleveralgorithms.hearst.submission;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.cleveralgorithms.hearst.DbIO;

public class StoreTypeTitleYearMonthModel implements SalesModel
{
	private final Map<Integer, String> storeStoreType = new HashMap<Integer, String>();	
	private final Map<String,Double> salesMap = new HashMap<String,Double>();
	
	public StoreTypeTitleYearMonthModel()
	{}
	
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
	
	public void load(){
		try {
			loadStoreChains();	
		} catch(Exception e) {
			throw new RuntimeException("Fatal exception preparing store chains", e);
		}
		
		try {
			loadSalesMap();	
		} catch(Exception e) {
			throw new RuntimeException("Fatal exception preparing chain sales map", e);
		}
	}
	
	public String getSalesString(String store, String title, String year, String month)
	{
		String type = storeStoreType.get(Integer.parseInt(store));
		if (storeStoreType == null) {
			throw new RuntimeException("No storetype for store " + store);
		}
		String key = toKey(type, title, year, month);
		Double sales = salesMap.get(key);
		if (sales == null) {
			return null;
		}
		return sales.toString();
	}
	
	protected void loadSalesMap() throws Exception
	{
		Connection conn = null;
		ResultSet rs = null;
		try {
			String query = "select * from template_storetype_vd2"; // no neg sales
			
			conn = DbIO.getConnection();
			rs = DbIO.executeReadOnlyQuery(conn, query);
			while(rs.next())  {
				String key = toKey(rs.getString("store_type"),rs.getInt("title_key"),rs.getInt("on_year"),rs.getInt("on_month"));
				Double value = rs.getDouble("sales_total");
				salesMap.put(key, value); 
			} 
			
			System.out.println("Sucessfully loaded "+salesMap.size()+" chain sales records.");
		} finally {
			try {if(rs!=null)rs.close();} catch(Exception e) {}
			try {if(conn!=null)conn.close();} catch(Exception e) {}
		}
	}
	
	public final static String toKey(String type, Integer title, Integer year, Integer month)
	{
		return type+"-"+title+"-"+year+"-"+month;
	}
	
	public final static String toKey(String type, String title, String year, String month)
	{
		return type+"-"+title+"-"+year+"-"+month;
	}
	
	protected void loadStoreChains() throws Exception
	{
		Connection conn = null;
		ResultSet rs = null;		
		try {
			String query = "select * from store_storetype_vd";
			
			conn = DbIO.getConnection();
			rs = DbIO.executeReadOnlyQuery(conn, query);
			while(rs.next())  {
				// get data
				Integer key = rs.getInt("store_key");			
				String value = rs.getString("store_type");
				storeStoreType.put(key, value);
			} 
			
			System.out.println("Sucessfully loaded "+storeStoreType.size()+" store-storetype relations.");
		} finally {
			try {if(rs!=null)rs.close();} catch(Exception e) {}
			try {if(conn!=null)conn.close();} catch(Exception e) {}
		}
	}
	
	public static void main(String[] args) {
		StoreTypeTitleYearMonthModel c = new StoreTypeTitleYearMonthModel();
		c.load();
	}
}
