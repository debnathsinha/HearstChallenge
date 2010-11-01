package com.cleveralgorithms.hearst.nn.experiments.maritalstatus;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class MaritalStatusChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"marital_status_yes", "marital_status_no", "marital_status_unknown"}; 
	}
}
