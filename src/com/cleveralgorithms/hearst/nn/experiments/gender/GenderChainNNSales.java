package com.cleveralgorithms.hearst.nn.experiments.gender;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class GenderChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"gender_male", "gender_female", 
				"gender_both","gender_unknown"}; 
	}
}
