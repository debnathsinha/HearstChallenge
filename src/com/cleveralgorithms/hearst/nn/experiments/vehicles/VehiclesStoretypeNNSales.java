package com.cleveralgorithms.hearst.nn.experiments.vehicles;

import com.cleveralgorithms.hearst.nn.experiments.storetype.StoretypeBoundedAncestor;

public class VehiclesStoretypeNNSales extends StoretypeBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"store_type",
				"number_of_vehicles_in_zip_zip_4", "number_of_hhs_with_a_vehicle", "number_of_hhs_without_a_vehicle",
				"number_of_new_cars", "number_of_new_light_trucks", "number_of_used_cars", "number_of_used_light_trucks",
				"avg_vehicle_msrp", "avg_current_vehicle_retail_valu"}; 
	}
}
