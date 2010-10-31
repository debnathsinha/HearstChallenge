package com.cleveralgorithms.hearst.nn.experiments.income;

import java.util.LinkedList;
import java.util.List;

import com.cleveralgorithms.hearst.nn.Configuration;
import com.cleveralgorithms.hearst.nn.ImpTestNearestNeighbourSales;

public class IncomeChainNNSales extends ImpTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"income_under__15000", "income__15000_to__24999", 
				"income__25000_to__34999","income__35000_to__49999","income__50000_to__74999","income__75000_to__99999",
				"income__100000_to__124999", "income__125000_to__149999", "income__150000_to__174999",
				"income__175000_to__199999", "income__200000_to__249999", "income__250000_", "income_unknown"};
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
		
		return euclideanDistance(v1,v2);
	}
	
	public static void main(String[] args) {
		new IncomeChainNNSales().run();
	}
	
}
