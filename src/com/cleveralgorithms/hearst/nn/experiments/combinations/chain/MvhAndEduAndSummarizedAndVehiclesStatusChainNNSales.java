package com.cleveralgorithms.hearst.nn.experiments.combinations.chain;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class MvhAndEduAndSummarizedAndVehiclesStatusChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				// mhv
				"mvh___under__101000", "mvh____101000_to__200000", "mvh____201000_to__300000",
				"mvh____301000_to__500000", "mvh____501000_to__999999", "mvh____1000000_", "mvh___unknown",
				// edu
				"education_high_school_diploma", "education_some_college", 
				"education_bachelor_degree","education_graduate_degree","education_less_than_high_school","education_unknown",
				// summarized
				"summarized_area_lvl_statistics_a", "summarized_area_lvl_statistics_b", 
				"summarized_area_lvl_statistics_c", "summarized_area_lvl_statistics_d", 
				"summarized_area_lvl_statistics_e", "summarized_area_lvl_statistics_f",
				"summarized_area_lvl_statistics_g", "summarized_area_lvl_statistics_h",
				// vehicles
				"number_of_vehicles_in_zip_zip_4", "number_of_hhs_with_a_vehicle", "number_of_hhs_without_a_vehicle",
				"number_of_new_cars", "number_of_new_light_trucks", "number_of_used_cars", "number_of_used_light_trucks",
				"avg_vehicle_msrp", "avg_current_vehicle_retail_valu"
				}; 
	}
}
