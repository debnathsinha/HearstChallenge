package com.cleveralgorithms.hearst;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbIO 
{
	public final static String DB_CONNECTION_STRING = "jdbc:mysql://localhost/hearst_challenge";
	public final static String DB_DRIVER = "com.mysql.jdbc.Driver";
	public final static String DB_USERNAME = "root";
	public final static String DB_PASSWORD = "";
	
	
	public static Connection getConnection() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException
	{
		Class.forName(DB_DRIVER).newInstance();
	    Connection conn = DriverManager.getConnection(DB_CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);	    
	    return conn;
	}
	
	public static ResultSet executeReadOnlyQuery(Connection conn, String query) throws SQLException
	{
		System.out.println(" sql: "+query);
		
		Statement statement = conn.createStatement();
		statement.setFetchSize(1000);	
		
	    ResultSet resultSet = statement.executeQuery(query);
	    resultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
	    resultSet.setFetchSize(1000);	
	    
	    return resultSet;
	}
}
