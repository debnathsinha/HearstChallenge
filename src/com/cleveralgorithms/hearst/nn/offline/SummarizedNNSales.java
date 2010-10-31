package com.cleveralgorithms.hearst.nn.offline;

import com.cleveralgorithms.hearst.nn.OfflineTestNearestNeighbourSales;

public class SummarizedNNSales extends OfflineTestNearestNeighbourSales {

	@Override
	protected String[] getFields() {
		return new String[]{
				"summarized_area_lvl_statistics_a", "summarized_area_lvl_statistics_b", 
				"summarized_area_lvl_statistics_c", "summarized_area_lvl_statistics_d", 
				"summarized_area_lvl_statistics_e", "summarized_area_lvl_statistics_f",
				"summarized_area_lvl_statistics_g", "summarized_area_lvl_statistics_h"}; 
	}

	@Override
	protected int getKNeighbours() {
		return 20;
	}

	public static void main(String[] args) {
		new SummarizedNNSales().compute();
	}
	
}
