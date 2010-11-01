package com.cleveralgorithms.hearst.nn.experiments.dwelling;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class DwellingTypeChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"dwelling_type_single_family_sfd", "dwelling_type_multi_fam_condo", 
				"dwelling_type_marginal_multi_fa","dwelling_type_po_box","dwelling_type_unknown"}; 
	}	
}
