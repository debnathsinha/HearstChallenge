package com.cleveralgorithms.hearst.nn;

import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales.OfflineConfiguration;
import com.cleveralgorithms.hearst.nn.experiments.combinations.storetype.SummarizedAndMhvStoretypeNNSales;


public class GenerateSubmissionNNData {

	public ImpTestNearestNeighbourSales getModel()
	{
//		ImpTestNearestNeighbourSales s = new VehiclesAndSummarizedChainNNSales();
//		s.setConfiguration(new OfflineConfiguration(10));
		
		ImpTestNearestNeighbourSales s = new SummarizedAndMhvStoretypeNNSales();
		s.setConfiguration(new OfflineConfiguration(7));
		
		return s;
	}

	public void run()
	{
		// get model
		ImpTestNearestNeighbourSales model = getModel();
		// set submission mode
		model.setTestMode(false);
		model.run();
		System.out.println("done");
	}
	
	
	public static void main(String[] args) {
		new GenerateSubmissionNNData().run();
	}
}
