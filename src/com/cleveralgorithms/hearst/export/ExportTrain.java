package com.cleveralgorithms.hearst.export;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;




/**
 * Export training stores
 * @author jasonb
 *
 */
public class ExportTrain {
	
	public final static String DB_CONNECTION_STRING = "jdbc:mysql://localhost/hearst_challenge";
	public final static String DB_DRIVER = "com.mysql.jdbc.Driver";
	public final static String DB_USERNAME = "root";
	public final static String DB_PASSWORD = "";
	
	public final static String EXPORT_FILENAME = "experiments/export/export_train.dat";
	
	protected FileWriter writer;
	
	public void run() 
	{
		Connection conn = null;
		try {
			conn = getConnection();
			openFile();
			
			Statement st = conn.createStatement();
			st.setFetchSize(1000);
		    ResultSet rs = st.executeQuery("select store_key,title_key,on_year,on_month,sales_total from template_mo");
		    int count=0;
		    StringBuilder buf = new StringBuilder();
		    while (rs.next()) 
		    {
		    	buf.append(rs.getInt("store_key"));
		    	buf.append(",");
		    	buf.append(rs.getInt("title_key"));
		    	buf.append(",");
		    	buf.append(rs.getInt("on_year"));
		    	buf.append(",");
		    	buf.append(rs.getInt("on_month"));
		    	buf.append(",");
		    	buf.append(rs.getDouble("sales_total"));
		    	buf.append("\n");
		    	
		    	count++;
		    	if ((count%20000)==0) {
		    		writeTextFile(buf.toString());
		    		buf = new StringBuilder(); 
		    		System.out.println(" "+count+" records");
		    	}
		    }
			
		    System.out.println("Wrote data to " + EXPORT_FILENAME);
		    System.out.println("done.");
			
		} catch(Exception e) {
			throw new RuntimeException("Fatal Exception: "+ e.getMessage(), e);
		} finally {
			try {conn.close();} catch(Exception e) {}
			try {closeFile();} catch(Exception e) {}
		}
	}
	
	
	
	protected void openFile() throws IOException
	{
		writer = new FileWriter(EXPORT_FILENAME);
	}
	
	protected void writeTextFile(String s) throws IOException
	{
		writer.write(s);
		writer.flush();
	}
	
	protected void closeFile() throws IOException
	{
		writer.close();
	}
	
	public Connection getConnection() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException
	{
		Class.forName(DB_DRIVER).newInstance();
	    Connection conn = DriverManager.getConnection(DB_CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
	    
	    return conn;
	}
	
	public static void main(String[] args) {
		new ExportTrain().run();
	}
	
}
