package com.cleveralgorithms.hearst.nn.experiments.combinations.chain;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales.OfflineConfiguration;
import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class BigComboChain2 extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				
				// age
//				"age_18", "age_19", "age_20", "age_21","age_22_to_24","age_25_to_29","age_30_to_34",
//				"age_35_to_39","age_40_to_44","age_45_to_49","age_50_to_54","age_55_to_59","age_60_to_64",
//				"age_65_to_69","age_70_to_75","age_75_","age_unknown",
				// edu
				"education_high_school_diploma", "education_some_college", 
				"education_bachelor_degree","education_graduate_degree","education_less_than_high_school","education_unknown",
				// household
//				"hsehld_status_head_of_household", "hsehld_status_spouse", "hsehld_status_young_adult",
//				"hsehld_status_elderly_individua","hsehld_status_other_adult",
				// marital
				"marital_status_yes", "marital_status_no", "marital_status_unknown",
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
	
	public List<Configuration> getConfigurations()
	{
		List<Configuration> list = new LinkedList<Configuration>();
		
//		list.add(new OfflineConfiguration(1));
//		list.add(new OfflineConfiguration(3));
//		list.add(new OfflineConfiguration(5));
//		list.add(new OfflineConfiguration(7));
//		list.add(new OfflineConfiguration(10));
//		list.add(new OfflineConfiguration(13));
//		list.add(new OfflineConfiguration(15));
//		list.add(new OfflineConfiguration(17));
//		list.add(new OfflineConfiguration(19));
//		list.add(new OfflineConfiguration(20));
//		list.add(new OfflineConfiguration(30));
		
		list.add(new OfflineConfiguration(5));
		list.add(new OfflineConfiguration(6));
		list.add(new OfflineConfiguration(7));
		list.add(new OfflineConfiguration(8));
		list.add(new OfflineConfiguration(9));
		list.add(new OfflineConfiguration(10));
		
		return list;
	}
}
