package com.cleveralgorithms.hearst.nn.experiments.homeowner;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class HomeownerChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"homeowner", "renter", "homeowner_unknown"}; 
	}
}
