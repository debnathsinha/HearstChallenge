package com.cleveralgorithms.hearst.nn.experiments.homeowner;

import com.cleveralgorithms.hearst.nn.experiments.storetype.StoretypeBoundedAncestor;

public class HomeownerStoretypeNNSales extends StoretypeBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"store_type",
				"homeowner", "renter", "homeowner_unknown"}; 
	}
}
