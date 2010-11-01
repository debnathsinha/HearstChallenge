package com.cleveralgorithms.hearst.nn.experiments.maritalstatus;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class MaritalStatusNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{"marital_status_yes", "marital_status_no", "marital_status_unknown"}; 
	}
}
