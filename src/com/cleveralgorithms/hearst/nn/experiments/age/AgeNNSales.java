package com.cleveralgorithms.hearst.nn.experiments.age;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.OfflineTestNearestNeighbourSales;

public class AgeNNSales extends OfflineTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{"age_18", "age_19", "age_20", "age_21","age_22_to_24","age_25_to_29","age_30_to_34",
				"age_35_to_39","age_40_to_44","age_45_to_49","age_50_to_54","age_55_to_59","age_60_to_64",
				"age_65_to_69","age_70_to_75","age_75_","age_unknown"}; 
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
		new AgeNNSales().run();
	}
	
}
