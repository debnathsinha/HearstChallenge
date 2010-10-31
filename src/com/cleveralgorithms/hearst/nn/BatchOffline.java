package com.cleveralgorithms.hearst.nn;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.OfflineTestNearestNeighbourSales.OfflineConfiguration;
import com.cleveralgorithms.hearst.nn.offline.ChainNNSales;




public class BatchOffline {

	public OfflineTestNearestNeighbourSales getModel()
	{
		return new ChainNNSales();
	}
	
	
	public void runBatch()
	{
		OfflineTestNearestNeighbourSales model = getModel();
		model.loadData();
		
		List<String> results = new LinkedList<String>();
		
		try {
			// run each configuration
			for(Configuration cfg : model.getConfigurations())
			{
				// configure
				model.setConfiguration((OfflineConfiguration)cfg);
				// execute
				model.compute();
				// get result
				String rs = model.getExperimentResultString();
				System.out.println(rs);
				results.add(rs);
			}
		} finally {
			printResults(results);
		}		
	}
	
	protected void printResults(List<String> results)
	{
		System.out.println();
		System.out.println("|_. Model |_. Coverage |_. RMSE |");
		for(String result : results) 
		{
			System.out.println(result);
		}
	}
	
	
	public static void main(String[] args) {
		new BatchOffline().runBatch();
	}
	
}
