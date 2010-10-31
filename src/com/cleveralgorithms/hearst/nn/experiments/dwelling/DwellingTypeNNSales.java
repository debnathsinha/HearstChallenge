package com.cleveralgorithms.hearst.nn.experiments.dwelling;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.OfflineTestNearestNeighbourSales;

public class DwellingTypeNNSales extends OfflineTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{"dwelling_type_single_family_sfd", "dwelling_type_multi_fam_condo", 
				"dwelling_type_marginal_multi_fa","dwelling_type_po_box","dwelling_type_unknown"}; 
	}

	public List<Configuration> getConfigurations()
	{
		List<Configuration> list = new LinkedList<Configuration>();
		
		list.add(new OfflineConfiguration(1));
		list.add(new OfflineConfiguration(3));
		list.add(new OfflineConfiguration(5));
		list.add(new OfflineConfiguration(10));
		list.add(new OfflineConfiguration(50));
		list.add(new OfflineConfiguration(100));
		list.add(new OfflineConfiguration(500));
		list.add(new OfflineConfiguration(1000));
		
		return list;
	}

	public static void main(String[] args) {
		new DwellingTypeNNSales().run();
	}
	
}
