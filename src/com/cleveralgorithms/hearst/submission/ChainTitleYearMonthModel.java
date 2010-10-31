package com.cleveralgorithms.hearst.submission;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.cleveralgorithms.hearst.DbIO;

public class ChainTitleYearMonthModel implements SalesModel
{
	private final Map<Integer, Integer> storeChains = new HashMap<Integer, Integer>();	
	private final Map<String,Double> salesMap = new HashMap<String,Double>();
	
	public ChainTitleYearMonthModel()
	{}
	
	public void load(){
		try {
			loadStoreChains();	
		} catch(Exception e) {
			throw new RuntimeException("Fatal exception preparing store chains", e);
		}
		
		try {
			loadChainSalesMap();	
		} catch(Exception e) {
			throw new RuntimeException("Fatal exception preparing chain sales map", e);
		}
	}
	
	public String getSalesString(String store, String title, String year, String month)
	{
		Integer chain = storeChains.get(Integer.parseInt(store));
		if (chain == null) {
			throw new RuntimeException("No chain for store " + store);
		}
		String key = toKey(chain.toString(), year, month);
		Double sales = salesMap.get(key);
		if (sales == null) {
			return null;
		}
		return sales.toString();
	}
	
	protected void loadChainSalesMap() throws Exception
	{
		Connection conn = null;
		ResultSet rs = null;		
		try {
			String query = "select * from chain_title_year_month_sales_td";
			
			conn = DbIO.getConnection();
			rs = DbIO.executeReadOnlyQuery(conn, query);
			while(rs.next())  {
				String key = toKey(rs.getInt("chain_key"),rs.getInt("on_year"),rs.getInt("on_month"));
				Double value = rs.getDouble("sales_total");
				salesMap.put(key, value);
			} 
			
			System.out.println("Sucessfully loaded "+salesMap.size()+" chain sales records.");
		} finally {
			try {if(rs!=null)rs.close();} catch(Exception e) {}
			try {if(conn!=null)conn.close();} catch(Exception e) {}
		}
	}
	
	public final static String toKey(Integer chain, Integer year, Integer month)
	{
		return chain+"-"+year+"-"+month;
	}
	
	public final static String toKey(String chain, String year, String month)
	{
		return chain+"-"+year+"-"+month;
	}
	
	protected void loadStoreChains() throws Exception
	{
		Connection conn = null;
		ResultSet rs = null;		
		try {
			String query = "select * from store_chain_vd";
			
			conn = DbIO.getConnection();
			rs = DbIO.executeReadOnlyQuery(conn, query);
			while(rs.next())  {
				// get data
				Integer key = rs.getInt("store_key");			
				Integer value = rs.getInt("chain_key");
				storeChains.put(key, value);
			} 
			
			System.out.println("Sucessfully loaded "+storeChains.size()+" store-chain relations.");
		} finally {
			try {if(rs!=null)rs.close();} catch(Exception e) {}
			try {if(conn!=null)conn.close();} catch(Exception e) {}
		}
	}
	
	public static void main(String[] args) {
		ChainTitleYearMonthModel c = new ChainTitleYearMonthModel();
		c.load();
	}
}
