package com.cleveralgorithms.hearst.nn.experiments.summarized;

import com.cleveralgorithms.hearst.nn.experiments.storetype.StoretypeBoundedAncestor;

public class SummarizedStoretypeNNSales extends StoretypeBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"store_type",
				"summarized_area_lvl_statistics_a", "summarized_area_lvl_statistics_b", 
				"summarized_area_lvl_statistics_c", "summarized_area_lvl_statistics_d", 
				"summarized_area_lvl_statistics_e", "summarized_area_lvl_statistics_f",
				"summarized_area_lvl_statistics_g", "summarized_area_lvl_statistics_h"}; 
	}
}
