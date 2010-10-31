package com.cleveralgorithms.hearst.nn;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.cleveralgorithms.hearst.FileIO;




public abstract class SubmissionNearestNeighbourSales extends DefaultNearestNeighbourSales
{
	protected void postComputation()
	{
		// write test sales NN data
		try {
			writeTestSalesData();
		} catch(Exception e) {
			throw new RuntimeException("Fatal Exception: " + e.getMessage(), e);
		}
	}
	
	protected void writeTestSalesData() throws IOException
	{
		StringBuilder buf = new StringBuilder();
		
		// process all title/year/months in test
		for(String key : testSales.keySet()) {
			Map<Integer,Double> salesData = testSales.get(key);
			
			// process all stores
			for(Integer storeKey : salesData.keySet()) {
				String [] parts = key.split("-");
				Double sales = salesData.get(storeKey);				
				buf.append(storeKey);
				buf.append(",");
				for (int i = 0; i < parts.length; i++) {
					buf.append(parts[i]);
					buf.append(",");
				}
				buf.append(sales.toString());
				buf.append("\n");
			}			
		}
		String filename = getOutputFilename();
		FileIO.writeStringToFile(buf.toString(), new File(filename));
		System.out.println("Successfully wrote file: " + filename);
	}
	
	protected abstract String getOutputFilename();

	@Override
	protected void loadTestingData() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
