package com.cleveralgorithms.hearst.nn.experiments.gender;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales.OfflineConfiguration;
import com.cleveralgorithms.hearst.nn.experiments.homeowner.HomeownerChainNNSales;

public class GenderNNSales extends ImpTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{"gender_male", "gender_female", 
				"gender_both","gender_unknown"}; 
	}
	
	public List<Configuration> getConfigurations()
	{
		List<Configuration> list = new LinkedList<Configuration>();
		
		list.add(new OfflineConfiguration(1));
		list.add(new OfflineConfiguration(3));
		list.add(new OfflineConfiguration(5));
		list.add(new OfflineConfiguration(10));
		list.add(new OfflineConfiguration(50));
		list.add(new OfflineConfiguration(100));
		list.add(new OfflineConfiguration(500));
		list.add(new OfflineConfiguration(1000));
		
		return list;
	}
	protected double calculateDistanceToStore(Integer trainStoreId, Integer testStoreId)
	{
		double [] v1 = trainStores.get(trainStoreId);
		double [] v2 = testStores.get(testStoreId);
		
		if (v1 == null || v1.length==0 || v2 == null || v2.length==0) {
			return Double.NaN;
		}

		if (v1[0] != v2[0]) {
			return Double.NaN;
		} 
		
		return euclideanDistance(v1,v2);
	}
}
