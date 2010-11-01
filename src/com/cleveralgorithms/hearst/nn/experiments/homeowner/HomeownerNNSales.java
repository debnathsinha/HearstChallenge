package com.cleveralgorithms.hearst.nn.experiments.homeowner;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class HomeownerNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{"homeowner", "renter", "homeowner_unknown"}; 
	}
}
