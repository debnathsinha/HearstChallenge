package com.cleveralgorithms.hearst.nn.experiments.income;

import com.cleveralgorithms.hearst.nn.experiments.chain.ChainBoundedAncestor;

public class IncomeChainNNSales extends ChainBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"chain_key",
				"income_under__15000", "income__15000_to__24999", 
				"income__25000_to__34999","income__35000_to__49999","income__50000_to__74999","income__75000_to__99999",
				"income__100000_to__124999", "income__125000_to__149999", "income__150000_to__174999",
				"income__175000_to__199999", "income__200000_to__249999", "income__250000_", "income_unknown"};
	}
}
