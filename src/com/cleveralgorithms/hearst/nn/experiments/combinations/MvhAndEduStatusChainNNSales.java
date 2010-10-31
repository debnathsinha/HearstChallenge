package com.cleveralgorithms.hearst.nn.experiments.combinations;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.Utils;
import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales.OfflineConfiguration;

public class MvhAndEduStatusChainNNSales extends ImpTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"education_high_school_diploma", "education_some_college", 
				"education_bachelor_degree","education_graduate_degree","education_less_than_high_school","education_unknown",
				"mvh___under__101000", "mvh____101000_to__200000", "mvh____201000_to__300000",
				"mvh____301000_to__500000", "mvh____501000_to__999999", "mvh____1000000_", "mvh___unknown"}; 
	}

	public List<Configuration> getConfigurations()
	{
		List<Configuration> list = new LinkedList<Configuration>();
		
		list.add(new OfflineConfiguration(1));
		list.add(new OfflineConfiguration(3));
		list.add(new OfflineConfiguration(5));
		list.add(new OfflineConfiguration(7));
		list.add(new OfflineConfiguration(10));
		list.add(new OfflineConfiguration(15));
		
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

		if (v1[0] != v2[0]) {
			return Double.NaN;
		} 
		
		return Utils.euclideanDistance(v1,v2,IGNORE_LIST);
	}
	
	public static void main(String[] args) {
		new MvhAndEduStatusChainNNSales().run();
	}
	
}
