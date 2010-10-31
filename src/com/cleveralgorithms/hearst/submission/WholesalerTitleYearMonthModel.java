package com.cleveralgorithms.hearst.submission;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.cleveralgorithms.hearst.DbIO;

public class WholesalerTitleYearMonthModel implements SalesModel
{
	private final Map<String, Integer> storeWholesalers = new HashMap<String, Integer>();	
	private final Map<String,Double> salesMap = new HashMap<String,Double>();
	
	public WholesalerTitleYearMonthModel()
	{}
	
	public void load(){
		try {
			loadStoreChains();	
		} catch(Exception e) {
			throw new RuntimeException("Fatal exception preparing store wholesalers", e);
		}
		
		try {
			loadSalesMap();	
		} catch(Exception e) {
			throw new RuntimeException("Fatal exception preparing chain sales map", e);
		}
	}
	
	public String getSalesString(String store, String title, String year, String month)
	{
		Integer wholesaler = storeWholesalers.get(toWholesalerKey(store, title));
		if (wholesaler == null) {
			throw new RuntimeException("No wholesaler for store " + store);
		}
		String key = toKey(wholesaler.toString(), title, year, month);
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
			String query = "select * from wholesaler_title_year_month_sales_vd";
			
			conn = DbIO.getConnection();
			rs = DbIO.executeReadOnlyQuery(conn, query);
			while(rs.next())  {
				String key = toKey(rs.getInt("wholesaler_key"),rs.getInt("title_key"),rs.getInt("on_year"),rs.getInt("on_month"));
				Double value = rs.getDouble("sales_total");
				salesMap.put(key, value);
			} 
			
			 System.out.println("Sucessfully loaded "+salesMap.size()+" title-wholesaler sales records.");
		} finally {
			try {if(rs!=null)rs.close();} catch(Exception e) {}
			try {if(conn!=null)conn.close();} catch(Exception e) {}
		}
	}
	
	public final static String toKey(Integer wholesaler, Integer title, Integer year, Integer month)
	{
		return wholesaler+"-"+title+"-"+year+"-"+month;
	}
	public final static String toKey(String wholesaler, String title, String year, String month)
	{
		return wholesaler+"-"+title+"-"+year+"-"+month;
	}

	
	
	public final static String toWholesalerKey(Integer store, Integer title)
	{
		return store+"-"+title;
	}
	public final static String toWholesalerKey(String store, String title)
	{
		return store+"-"+title;
	}
	
	protected void loadStoreChains() throws Exception
	{
		Connection conn = null;
		ResultSet rs = null;		
		try {
			String query = "select * from wholesaler_store_vd";
			
			conn = DbIO.getConnection();
			rs = DbIO.executeReadOnlyQuery(conn, query);
			while(rs.next())  {
				// get data
				Integer store = rs.getInt("store_key");
				Integer title = rs.getInt("title_key");
				String key = toWholesalerKey(store, title);
				Integer value = rs.getInt("wholesaler_key");
				storeWholesalers.put(key, value);
			} 
			
			System.out.println("Sucessfully loaded "+storeWholesalers.size()+" store-wholesaler relations.");
		} finally {
			try {if(rs!=null)rs.close();} catch(Exception e) {}
			try {if(conn!=null)conn.close();} catch(Exception e) {}
		}
	}
	
	public static void main(String[] args) {
		WholesalerTitleYearMonthModel c = new WholesalerTitleYearMonthModel();
		c.load();
	}
}
