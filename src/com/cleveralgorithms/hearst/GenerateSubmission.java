package com.cleveralgorithms.hearst;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GenerateSubmission 
{

	public final static String SOURCE_FILENAME = "dat/summarized_nn_sales_data.csv";
	public final static String TEMPLATE_FILENAME = "data/template_for_submission.csv";	
	public final static String OUTPUT_FILENAME = "submissions/summarized_nn_sales_data.csv";
	
	
	public void run() throws Exception
	{		
		// source
		Map<String,String> salesMap = loadSourceData(SOURCE_FILENAME);
		// template
		String templateData = createOutputString(TEMPLATE_FILENAME, salesMap);		
		// write file
		writeOutput(OUTPUT_FILENAME, templateData);
		
		System.out.println("done");
	}
	
	protected void writeOutput(String filename, String data)
		throws Exception
	{
		FileIO.writeStringToFile(data, new File(filename));
		System.out.println("Wrote file: " + filename);
	}
	
	protected String createOutputString(String templateFilename, Map<String,String> salesMap)
		throws Exception
	{
		System.out.println("Processing template data " + templateFilename);
		String templateData = FileIO.fastLoadFileAsString(new File(templateFilename));
		String [] lines = templateData.split("\n");
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (i ==0 ) {
				buf.append(line);
				buf.append("\n");
				continue; 
			}
			String [] lineParts = line.trim().split(",");
			String key = toSalesKey(lineParts[0].trim(), lineParts[1].trim(), lineParts[2].trim());
			String salesTotal = salesMap.get(key);
			if (salesTotal == null) {
				throw new RuntimeException("No data for line "+ i+ "[key="+key+"], " + line);
			}
			addLine(buf, lineParts[0], lineParts[1], lineParts[2], salesTotal);
			
			if ((i%20000)==0) {	    		
	    		System.out.println(" "+i+" records");
	    	}
		}
		
		return buf.toString();
	}
	
	protected void addLine(StringBuilder buf, String storeKey, String titleKey, String yearMonth, String salesTotal)
	{
		buf.append(storeKey.trim());
		buf.append(",");
		buf.append(titleKey.trim());
		buf.append(",");
		buf.append(yearMonth.trim());
		buf.append(",");
		buf.append(salesTotal.trim());
		buf.append("\n");
	}
	
	protected Map<String,String> loadSourceData(String filename) throws IOException
	{
		System.out.println("Loading source data " + filename);
		Map<String,String> map = new HashMap<String,String>();
		
		String salesData = FileIO.fastLoadFileAsString(new File(filename));
		String [] lines = salesData.split("\n");
		for(String line : lines)
		{			
			String [] lineParts = line.split(",");
			if (lineParts.length != 5) {
				throw new RuntimeException("Bad line: " + line);
			}
			String key = toSalesKey(lineParts[0], lineParts[1], lineParts[2], lineParts[3]);
			String value = lineParts[4];
			map.put(key, value);
		}
		return map;
	}
	
	public final static String toSalesKey(String storeKey, String titleKey, String yearKey, String monthKey)
	{
		return Integer.parseInt(storeKey) +"-"+
			   Integer.parseInt(titleKey)+"-"+
			   Integer.parseInt(yearKey)+"-"+
			   Integer.parseInt(monthKey);
	}
	public final static String toSalesKey(String storeKey, String titleKey, String yearMonth)
	{
		String year = yearMonth.substring(0,4);
		String month = yearMonth.substring(4);
		return toSalesKey(storeKey, titleKey, year, month);
	}
	
	public static void main(String[] args) throws Exception 
	{
		new GenerateSubmission().run();
	}
}
