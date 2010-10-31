package com.cleveralgorithms.hearst.nn;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.cleveralgorithms.hearst.nn.OfflineTestNearestNeighbourSales.OfflineConfiguration;
import com.cleveralgorithms.hearst.nn.experiments.age.AgeNNSales;
import com.cleveralgorithms.hearst.nn.experiments.dwelling.DwellingSizeNNSales;
import com.cleveralgorithms.hearst.nn.experiments.dwelling.DwellingTypeNNSales;
import com.cleveralgorithms.hearst.nn.experiments.education.EducationNNSales;
import com.cleveralgorithms.hearst.nn.experiments.gender.GenderNNSales;
import com.cleveralgorithms.hearst.nn.experiments.homeowner.HomeownerNNSales;
import com.cleveralgorithms.hearst.nn.experiments.household.HouseHoldStatusNNSales;
import com.cleveralgorithms.hearst.nn.experiments.income.IncomeNNSales;
import com.cleveralgorithms.hearst.nn.experiments.maritalstatus.MaritalStatusNNSales;
import com.cleveralgorithms.hearst.nn.experiments.mvh.MvhStatusNNSales;
import com.cleveralgorithms.hearst.nn.experiments.occupation.OccupationNNSales;
import com.cleveralgorithms.hearst.nn.experiments.residence.ResidenceNNSales;
import com.cleveralgorithms.hearst.nn.experiments.size.SizeNNSales;
import com.cleveralgorithms.hearst.nn.experiments.summarized.SummarizedChainNNSales;
import com.cleveralgorithms.hearst.nn.experiments.vehicles.VehiclesNNSales;




public class BatchOffline {

	protected static Logger logger = Logger.getLogger(BatchOffline.class.getName());
	
	
	public List<OfflineTestNearestNeighbourSales> getModels()
	{
		List<OfflineTestNearestNeighbourSales> list = new LinkedList<OfflineTestNearestNeighbourSales>();
		
		// baseline
//		list.add(new Baseline());
//		// chain
//		list.add(new ChainNNSales());
//		// summarized
//		list.add(new SummarizedNNSales());
		list.add(new SummarizedChainNNSales());
		// other linear models
		list.add(new AgeNNSales());
		list.add(new DwellingSizeNNSales());
		list.add(new DwellingTypeNNSales());
		list.add(new EducationNNSales());
		list.add(new GenderNNSales());
		list.add(new HomeownerNNSales());
		list.add(new HouseHoldStatusNNSales());
		list.add(new IncomeNNSales());
		list.add(new MaritalStatusNNSales());
		list.add(new MvhStatusNNSales());
		list.add(new OccupationNNSales());
		list.add(new ResidenceNNSales());
		list.add(new SizeNNSales());
		list.add(new VehiclesNNSales());
		
		
		return list;
	}
	
	
	
	public void log(String msg) {
		logger.info(msg);
	}
	
	public void runBatch()
	{
		List<OfflineTestNearestNeighbourSales> models = getModels();
		
		// process each model in turn
		for(OfflineTestNearestNeighbourSales model : models) {
			List<String> results = new LinkedList<String>();
			
			try {
				log("Model: " + model.getClass().getSimpleName());
				
				model.loadData();
				// run each configuration
				for(Configuration cfg : model.getConfigurations())
				{
					// configure
					model.setConfiguration((OfflineConfiguration)cfg);
					model.clearPredictions();
					// execute
					model.compute();
					// get result
					String rs = model.getExperimentResultString();
					System.out.println(rs);
					results.add(rs);
				}
			} finally {
				saveResults(results);
			}	
		}
		
		log("done.");
	}
	
	protected void saveResults(List<String> results)
	{		
		log("|_. Model |_. Coverage |_. RMSE |");
		for(String result : results) 
		{
			log(result);
		}
		log("");
	}
	
	
	public static void main(String[] args) {
		try {
		    FileHandler handler = new FileHandler("experiments.log");
		    handler.setFormatter(new SimplerFormatter());			    
		    logger.addHandler(handler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new BatchOffline().runBatch();
	}
	
	public static class SimplerFormatter extends Formatter
	{
		public String format(LogRecord rec) {	    	
	    	return rec.getMessage() + "\n";
	    }
	}
}
