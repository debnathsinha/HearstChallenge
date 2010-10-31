package com.cleveralgorithms.hearst.submission;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.cleveralgorithms.hearst.FileIO;

public class GenerateSubmission 
{
	public final static String TEMPLATE_FILENAME = "data/template_for_submission.csv";	
	
	
	public List<SalesModel> getSalesModels()
	{
		List<SalesModel> list = new LinkedList<SalesModel>();
				
		// model cascade
		list.add(new NNModel(""));
		list.add(new ChainTitleYearMonthModel());
		list.add(new StoreTypeTitleYearMonthModel());
		list.add(new WholesalerTitleYearMonthModel());
		
		// load 
		for(SalesModel m : list) {
			m.load();
		}
		
		return list;
	}
	
	protected String getOutputFilename()
	{
		return "submissions/submission"+System.currentTimeMillis()+".csv";
	}
	
	
	public void run() throws Exception
	{		
		// cascade
		List<SalesModel> models = getSalesModels();
		
		// build template
		String templateData = createOutputString(TEMPLATE_FILENAME, models);		
		// write file
		writeOutput(getOutputFilename(), templateData);
		
		System.out.println("done");
	}
	
	protected void writeOutput(String filename, String data)
		throws Exception
	{
		FileIO.writeStringToFile(data, new File(filename));
		System.out.println("Wrote file: " + filename);
	}
	
	protected String createOutputString(String templateFilename, List<SalesModel> list)
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
	
	protected String getSalesString(List<SalesModel> list, String store, String title, String yearMonth)
	{
		String year = yearMonth.substring(0,4);
		String month = yearMonth.substring(4);
		
		String sales = null;
		
		for(SalesModel m : list) {
			sales = m.getSalesString(store, title, year, month);
			if (sales != null) {
				break;
			}
		}
		
		if (sales == null) {
			throw new RuntimeException("Could not find sales data for record");
		}
		
		return sales;
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
	
	public static void main(String[] args) throws Exception 
	{
		new GenerateSubmission().run();
	}
	
}
