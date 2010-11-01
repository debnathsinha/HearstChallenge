package com.cleveralgorithms.hearst.nn.experiments.maritalstatus;

import com.cleveralgorithms.hearst.nn.experiments.storetype.StoretypeBoundedAncestor;

public class MaritalStatusStoretypeNNSales extends StoretypeBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"store_type",
				"marital_status_yes", "marital_status_no", "marital_status_unknown"}; 
	}
}
