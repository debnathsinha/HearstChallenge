package com.cleveralgorithms.hearst.nn.experiments.dwelling;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class DwellingSizeChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"dwelling_size___1_unit", "dwelling_size___2_units", 
				"dwelling_size___3_units","dwelling_size___4_units","dwelling_size___5_to_9_units",
				"dwelling_size___10_to_19_units","dwelling_size___20_to_49_units","dwelling_size___50_to_100_units","dwelling_size___100__units",
				"dwelling_size___unknown"}; 
	}
}
