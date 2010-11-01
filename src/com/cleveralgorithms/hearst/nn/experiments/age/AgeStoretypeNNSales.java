package com.cleveralgorithms.hearst.nn.experiments.age;

import com.cleveralgorithms.hearst.nn.experiments.storetype.StoretypeBoundedAncestor;

public class AgeStoretypeNNSales extends StoretypeBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"store_type",
				"age_18", "age_19", "age_20", "age_21","age_22_to_24","age_25_to_29","age_30_to_34",
				"age_35_to_39","age_40_to_44","age_45_to_49","age_50_to_54","age_55_to_59","age_60_to_64",
				"age_65_to_69","age_70_to_75","age_75_","age_unknown"}; 
	}
}
