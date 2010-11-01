package com.cleveralgorithms.hearst.nn.experiments.mvh;

import com.cleveralgorithms.hearst.nn.experiments.storetype.StoretypeBoundedAncestor;

public class MvhStatusStoretypeNNSales extends StoretypeBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"store_type",
				"mvh___under__101000", "mvh____101000_to__200000", "mvh____201000_to__300000",
				"mvh____301000_to__500000", "mvh____501000_to__999999", "mvh____1000000_", "mvh___unknown"}; 
	}
}
