package com.cleveralgorithms.hearst.nn.experiments.combinations.chain;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class SummarizedAndMhvChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"mvh___under__101000", "mvh____101000_to__200000", "mvh____201000_to__300000",
				"mvh____301000_to__500000", "mvh____501000_to__999999", "mvh____1000000_", "mvh___unknown",
				"summarized_area_lvl_statistics_a", "summarized_area_lvl_statistics_b", 
				"summarized_area_lvl_statistics_c", "summarized_area_lvl_statistics_d", 
				"summarized_area_lvl_statistics_e", "summarized_area_lvl_statistics_f",
				"summarized_area_lvl_statistics_g", "summarized_area_lvl_statistics_h"}; 
	}	
}
