package com.cleveralgorithms.hearst.nn;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales.OfflineConfiguration;
import com.cleveralgorithms.hearst.nn.experiments.combinations.MvhAndEduAndSummarizedAndVehiclesStatusChainNNSales;
import com.cleveralgorithms.hearst.nn.experiments.combinations.MvhAndEduStatusChainNNSales;
import com.cleveralgorithms.hearst.nn.experiments.combinations.MvhStatusAndVehiclesChainNNSales;
import com.cleveralgorithms.hearst.nn.experiments.combinations.SummarizedAndEduChainNNSales;
import com.cleveralgorithms.hearst.nn.experiments.combinations.SummarizedAndMhvChainNNSales;
import com.cleveralgorithms.hearst.nn.experiments.combinations.VehiclesAndEducationChainNNSales;
import com.cleveralgorithms.hearst.nn.experiments.combinations.VehiclesAndSummarizedChainNNSales;




public class BatchOffline {

	protected static Logger logger = Logger.getLogger(BatchOffline.class.getName());
	
	
	public List<ImpTestNearestNeighbourSales> getModels()
	{
		List<ImpTestNearestNeighbourSales> list = new LinkedList<ImpTestNearestNeighbourSales>();
		
//		// baseline
//		list.add(new Baseline());
//
//		// other linear models		
//		list.add(new AgeNNSales());
//		list.add(new DwellingSizeNNSales());
//		list.add(new DwellingTypeNNSales());
//		list.add(new EducationNNSales());
//		list.add(new GenderNNSales());
//		list.add(new HomeownerNNSales());
//		list.add(new HouseholdStatusNNSales());
//		list.add(new IncomeNNSales());
//		list.add(new MaritalStatusNNSales());
//		list.add(new MvhStatusNNSales());
//		list.add(new OccupationNNSales());
//		list.add(new ResidenceNNSales());
//		list.add(new SizeNNSales());
//		list.add(new SummarizedNNSales());
//		list.add(new VehiclesNNSales());
//		
//		// chain
//		list.add(new ChainNNSales());
		
		// linear + chain
//		list.add(new AgeChainNNSales());
//		list.add(new DwellingSizeChainNNSales());
//		list.add(new DwellingTypeChainNNSales());
//		list.add(new EducationChainNNSales());
//		list.add(new GenderChainNNSales());
//		list.add(new HomeownerChainNNSales());
//		list.add(new HouseholdChainStatusNNSales());
//		list.add(new IncomeChainNNSales());
//		list.add(new MaritalStatusChainNNSales());
//		list.add(new MvhStatusChainNNSales());
//		list.add(new OccupationChainNNSales());
//		list.add(new ResidenceChainNNSales());
//		list.add(new SizeChainNNSales());
//		list.add(new SummarizedChainNNSales());
//		list.add(new VehiclesChainNNSales());
		
		// combinations
//		list.add(new MvhAndEduStatusChainNNSales());
//		list.add(new MvhStatusAndVehiclesChainNNSales());
//		list.add(new SummarizedAndEduChainNNSales());
//		list.add(new SummarizedAndMhvChainNNSales());
//		list.add(new VehiclesAndEducationChainNNSales());
		list.add(new VehiclesAndSummarizedChainNNSales());
//		list.add(new MvhAndEduAndSummarizedAndVehiclesStatusChainNNSales()); // all 4
		
		return list;
	}
	
	
	
	public void log(String msg) {
		logger.info(msg);
	}
	
	public void runBatch()
	{
		List<ImpTestNearestNeighbourSales> models = getModels();
		
		// process each model in turn
		for (Iterator<ImpTestNearestNeighbourSales> it = models.iterator(); it.hasNext();) {
			ImpTestNearestNeighbourSales model = (ImpTestNearestNeighbourSales) it.next();
			it.remove(); // ensure it can be GC'd
			
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
