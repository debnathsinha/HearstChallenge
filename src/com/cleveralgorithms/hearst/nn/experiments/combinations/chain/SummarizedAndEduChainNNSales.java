package com.cleveralgorithms.hearst.nn.experiments.combinations.chain;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class SummarizedAndEduChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"summarized_area_lvl_statistics_a", "summarized_area_lvl_statistics_b", 
				"summarized_area_lvl_statistics_c", "summarized_area_lvl_statistics_d", 
				"summarized_area_lvl_statistics_e", "summarized_area_lvl_statistics_f",
				"summarized_area_lvl_statistics_g", "summarized_area_lvl_statistics_h",
				"education_high_school_diploma", "education_some_college", 
				"education_bachelor_degree","education_graduate_degree","education_less_than_high_school","education_unknown"}; 
	}	
}
