package com.cleveralgorithms.hearst.nn.experiments.combinations.chain;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class MvhAndEduStatusChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"education_high_school_diploma", "education_some_college", 
				"education_bachelor_degree","education_graduate_degree","education_less_than_high_school","education_unknown",
				"mvh___under__101000", "mvh____101000_to__200000", "mvh____201000_to__300000",
				"mvh____301000_to__500000", "mvh____501000_to__999999", "mvh____1000000_", "mvh___unknown"}; 
	}

}
