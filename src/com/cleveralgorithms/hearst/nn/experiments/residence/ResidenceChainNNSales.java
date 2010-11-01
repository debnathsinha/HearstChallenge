package com.cleveralgorithms.hearst.nn.experiments.residence;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class ResidenceChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"length_of_residence_under_1_yr", "length_of_residence_1_yr", 
				"length_of_residence_2_to_5_yrs","length_of_residence_6_to_10_yrs","length_of_residence_10__yrs","length_of_residence_unknown"}; 
	}
}
