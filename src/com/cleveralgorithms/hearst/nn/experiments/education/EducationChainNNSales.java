package com.cleveralgorithms.hearst.nn.experiments.education;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class EducationChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"education_high_school_diploma", "education_some_college", 
				"education_bachelor_degree","education_graduate_degree","education_less_than_high_school","education_unknown"}; 
	}
}
