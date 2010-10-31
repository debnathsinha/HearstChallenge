package com.cleveralgorithms.hearst.nn.experiments;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales;

public class Baseline extends ImpTestNearestNeighbourSales 
{
	public List<Configuration> getConfigurations()
	{
		List<Configuration> list = new LinkedList<Configuration>();
		
		list.add(new OfflineConfiguration(999999));
		
		return list;
	}
	
	
	@Override
	protected String[] getFields() {
		return new String[]{"chain_key"}; 
	}

	protected double calculateDistanceToStore(Integer trainStoreId, Integer testStoreId)
	{
		double [] v1 = trainStores.get(trainStoreId);
		double [] v2 = testStores.get(testStoreId);
		
		if (v1 == null || v1.length==0 || v2 == null || v2.length==0) {
			return Double.NaN;
		}
		
		return 1;
	}
	
	public static void main(String[] args) {  
		new Baseline().run();
	}
}
