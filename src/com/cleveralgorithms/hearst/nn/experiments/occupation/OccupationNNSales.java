package com.cleveralgorithms.hearst.nn.experiments.occupation;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales;

public class OccupationNNSales extends ImpTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{"occupation_prof_technical", "occupation_sales_service", 
				"occupation_farm_related","occupation_blue_collar","occupation_other","occupation_retired","occupation_unknown"}; 
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
		new OccupationNNSales().run();
	}
	
}
