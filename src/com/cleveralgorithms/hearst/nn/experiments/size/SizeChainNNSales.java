package com.cleveralgorithms.hearst.nn.experiments.size;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class SizeChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"households", "individuals"}; 
	}
}
