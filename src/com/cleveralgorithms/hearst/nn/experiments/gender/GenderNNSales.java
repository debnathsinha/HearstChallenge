package com.cleveralgorithms.hearst.nn.experiments.gender;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales;

public class GenderNNSales extends ImpTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{"gender_male", "gender_female", 
				"gender_both","gender_unknown"}; 
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
		new GenderNNSales().run();
	}
	
}