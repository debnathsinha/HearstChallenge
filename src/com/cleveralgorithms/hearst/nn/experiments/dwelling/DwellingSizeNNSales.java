package com.cleveralgorithms.hearst.nn.experiments.dwelling;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.OfflineTestNearestNeighbourSales;

public class DwellingSizeNNSales extends OfflineTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{"dwelling_size___1_unit", "dwelling_size___2_units", 
				"dwelling_size___3_units","dwelling_size___4_units","dwelling_size___5_to_9_units",
				"dwelling_size___10_to_19_units","dwelling_size___20_to_49_units","dwelling_size___50_to_100_units","dwelling_size___100__units",
				"dwelling_size___unknown"}; 
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
		new DwellingSizeNNSales().run();
	}
	
}
