package com.cleveralgorithms.hearst.nn.experiments.occupation;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class OccupationChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"occupation_prof_technical", "occupation_sales_service", 
				"occupation_farm_related","occupation_blue_collar","occupation_other","occupation_retired","occupation_unknown"}; 
	}
}
