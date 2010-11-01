package com.cleveralgorithms.hearst.nn.experiments.chain;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.Utils;
import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales;

public abstract class ChainBoundedAncestor extends ImpTestNearestNeighbourSales {

	public List<Configuration> getConfigurations()
	{
		List<Configuration> list = new LinkedList<Configuration>();
		
		list.add(new OfflineConfiguration(1));
		list.add(new OfflineConfiguration(3));
		list.add(new OfflineConfiguration(5));
		list.add(new OfflineConfiguration(7));
		list.add(new OfflineConfiguration(10));
		list.add(new OfflineConfiguration(15));
		list.add(new OfflineConfiguration(20));
		
		return list;
	}

	private final static int [] IGNORE_LIST = new int[]{0}; // ignore the chain
	
	protected int [] normalizeIgnoreIndices()
	{
		return IGNORE_LIST; 
	}
	

	protected double calculateDistanceToStore(Integer trainStoreId, Integer testStoreId)
	{
		double [] v1 = trainStores.get(trainStoreId);
		double [] v2 = testStores.get(testStoreId);
		
		if (v1 == null || v1.length==0 || v2 == null || v2.length==0) {
			return Double.NaN;
		}

		// chain must match
		if (v1[0] != v2[0]) {
			return Double.NaN;
		} 
		
		// distance ignoring the chain
		return Utils.euclideanDistance(v1,v2,IGNORE_LIST);
	}	
}
