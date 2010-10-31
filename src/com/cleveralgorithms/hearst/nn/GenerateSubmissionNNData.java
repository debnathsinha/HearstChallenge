package com.cleveralgorithms.hearst.nn;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainNNSales;


public class GenerateSubmissionNNData {

	public ImpTestNearestNeighbourSales getModel()
	{
		return new ChainNNSales();
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
