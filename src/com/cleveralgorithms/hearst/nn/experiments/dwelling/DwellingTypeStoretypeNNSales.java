package com.cleveralgorithms.hearst.nn.experiments.dwelling;

import com.cleveralgorithms.hearst.nn.experiments.storetype.StoretypeBoundedAncestor;

public class DwellingTypeStoretypeNNSales extends StoretypeBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"store_type",
				"dwelling_type_single_family_sfd", "dwelling_type_multi_fam_condo", 
				"dwelling_type_marginal_multi_fa","dwelling_type_po_box","dwelling_type_unknown"}; 
	}	
}
