package com.cleveralgorithms.hearst.nn.experiments.gender;

import com.cleveralgorithms.hearst.nn.experiments.storetype.StoretypeBoundedAncestor;

public class GenderStoretypeNNSales extends StoretypeBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"store_type",
				"gender_male", "gender_female", 
				"gender_both","gender_unknown"}; 
	}
}
