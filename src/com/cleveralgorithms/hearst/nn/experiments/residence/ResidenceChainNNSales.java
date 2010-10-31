package com.cleveralgorithms.hearst.nn.experiments.residence;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales;

public class ResidenceChainNNSales extends ImpTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"length_of_residence_under_1_yr", "length_of_residence_1_yr", 
				"length_of_residence_2_to_5_yrs","length_of_residence_6_to_10_yrs","length_of_residence_10__yrs","length_of_residence_unknown"}; 
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
	
	public static void main(String[] args) {
		new ResidenceChainNNSales().run();
	}
	
}
