package com.cleveralgorithms.hearst.nn.experiments.storetype;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales;

public class StoretypeNNSales extends ImpTestNearestNeighbourSales 
{
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
		list.add(new OfflineConfiguration(999999));
		
		return list;
	}
	
	
	@Override
	protected String[] getFields() {
		return new String[]{"store_type"}; 
	}

	protected double calculateDistanceToStore(Integer trainStoreId, Integer testStoreId)
	{
		double [] v1 = trainStores.get(trainStoreId);
		double [] v2 = testStores.get(testStoreId);
		
		if (v1 == null || v1.length==0 || v2 == null || v2.length==0) {
			return Double.NaN;
		}
		
		return (v1[0]==v2[0]) ? 1 : Double.NaN;
	}
	
	public static void main(String[] args) {  
		new StoretypeNNSales().run();
	}
}
