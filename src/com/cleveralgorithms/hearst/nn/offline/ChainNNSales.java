package com.cleveralgorithms.hearst.nn.offline;

import com.cleveralgorithms.hearst.nn.OfflineTestNearestNeighbourSales;

public class ChainNNSales extends OfflineTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{"chain_key"}; 
	}

	@Override
	protected int getKNeighbours() {
		return Integer.MAX_VALUE;
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
		new ChainNNSales().compute();
	}
	
}
