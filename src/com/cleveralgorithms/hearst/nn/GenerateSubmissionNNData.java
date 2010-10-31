package com.cleveralgorithms.hearst.nn;

import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales.OfflineConfiguration;
import com.cleveralgorithms.hearst.nn.experiments.combinations.VehiclesAndSummarizedChainNNSales;


public class GenerateSubmissionNNData {

	public ImpTestNearestNeighbourSales getModel()
	{
		VehiclesAndSummarizedChainNNSales s = new VehiclesAndSummarizedChainNNSales();
		s.setConfiguration(new OfflineConfiguration(10));
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
