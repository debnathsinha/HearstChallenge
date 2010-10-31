package com.cleveralgorithms.hearst.submission;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.cleveralgorithms.hearst.FileIO;

public class NNModel implements SalesModel 
{

	private final Map<String,String> nnSalesModel = new HashMap<String,String>();	
	private final String filename;
	
	public NNModel(String filename)
	{
		this.filename = filename;
	}

	public void load()
	{
		String salesData = null;		
		try {
			salesData = FileIO.fastLoadFileAsString(new File(filename));
		} catch (IOException e) {
			throw new RuntimeException("Unable to load nn sales data from file "+filename, e);
		}
		
		String [] lines = salesData.split("\n");
		for(String line : lines)
		{			
			String [] lineParts = line.split(",");
			if (lineParts.length != 5) {
				throw new RuntimeException("Bad line: " + line);
			}
			String key = toSalesKey(lineParts[0], lineParts[1], lineParts[2], lineParts[3]);
			String value = lineParts[4];
			nnSalesModel.put(key, value);
		}		
		System.out.println("Loaded "+nnSalesModel.size()+" NN sales records from " + filename);
	}
	
	public String getSalesStringForKey(String key)
	{
		return nnSalesModel.get(key);
	}
	
	public String getSalesString(String store, String title, String year, String month)
	{
		String key = toSalesKey(store, title, year, month);
		return getSalesStringForKey(key);
	}
	
	private final static String toSalesKey(String storeKey, String titleKey, String yearKey, String monthKey)
	{
		return Integer.parseInt(storeKey) +"-"+
			   Integer.parseInt(titleKey)+"-"+
			   Integer.parseInt(yearKey)+"-"+
			   Integer.parseInt(monthKey);
	}
	
	public static void main(String[] args) {
		NNModel n = new NNModel("dat/...");
		n.load();
	}
}
