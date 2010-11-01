package com.cleveralgorithms.hearst.nn.experiments.occupation;

import com.cleveralgorithms.hearst.nn.experiments.storetype.StoretypeBoundedAncestor;

public class OccupationStoretypeNNSales extends StoretypeBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"store_type",
				"occupation_prof_technical", "occupation_sales_service", 
				"occupation_farm_related","occupation_blue_collar","occupation_other","occupation_retired","occupation_unknown"}; 
	}
}
