package com.cleveralgorithms.hearst.nn.experiments.household;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class HouseholdChainStatusNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"hsehld_status_head_of_household", "hsehld_status_spouse", "hsehld_status_young_adult",
				"hsehld_status_elderly_individua","hsehld_status_other_adult"}; 
	}
}
