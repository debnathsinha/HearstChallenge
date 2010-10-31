package com.cleveralgorithms.hearst.nn.offline;

import com.cleveralgorithms.hearst.nn.OfflineTestNearestNeighbourSales;

public class ChainSummarizedNNSales extends OfflineTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"summarized_area_lvl_statistics_a", "summarized_area_lvl_statistics_b", 
				"summarized_area_lvl_statistics_c", "summarized_area_lvl_statistics_d", 
				"summarized_area_lvl_statistics_e", "summarized_area_lvl_statistics_f",
				"summarized_area_lvl_statistics_g", "summarized_area_lvl_statistics_h"}; 
	}

	@Override
	protected int getKNeighbours() {
		return 20;
	}
	
	protected double calculateDistanceToStore(Integer trainStoreId, Integer testStoreId)
	{
		double [] v1 = trainStores.get(trainStoreId);
		double [] v2 = testStores.get(testStoreId);
		
		if (v1 == null || v1.length==0 || v2 == null || v2.length==0) {
			return Double.NaN;
		}
		
		double dist = euclideanDistance(v1,v2);
		
		if (v1[0] != v2[0]) {
			dist += 100; // penality for a different chain
		} 
		
		return dist;
	}
	

	public static void main(String[] args) {
		new ChainSummarizedNNSales().compute();
	}	
}
