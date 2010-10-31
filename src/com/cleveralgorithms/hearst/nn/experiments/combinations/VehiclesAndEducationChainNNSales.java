package com.cleveralgorithms.hearst.nn.experiments.combinations;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales.OfflineConfiguration;

public class VehiclesAndEducationChainNNSales extends ImpTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"number_of_vehicles_in_zip_zip_4", "number_of_hhs_with_a_vehicle", "number_of_hhs_without_a_vehicle",
				"number_of_new_cars", "number_of_new_light_trucks", "number_of_used_cars", "number_of_used_light_trucks",
				"avg_vehicle_msrp", "avg_current_vehicle_retail_valu",
				"education_high_school_diploma", "education_some_college", 
				"education_bachelor_degree","education_graduate_degree","education_less_than_high_school","education_unknown"}; 
	}

	public List<Configuration> getConfigurations()
	{
		List<Configuration> list = new LinkedList<Configuration>();
		
		list.add(new OfflineConfiguration(1));
		list.add(new OfflineConfiguration(3));
		list.add(new OfflineConfiguration(5));
		list.add(new OfflineConfiguration(7));
		list.add(new OfflineConfiguration(10));
		list.add(new OfflineConfiguration(15));
		
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
		new VehiclesAndEducationChainNNSales().run();
	}
	
}