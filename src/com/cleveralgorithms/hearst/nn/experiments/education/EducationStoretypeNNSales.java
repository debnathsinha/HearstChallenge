package com.cleveralgorithms.hearst.nn.experiments.education;

import com.cleveralgorithms.hearst.nn.experiments.storetype.StoretypeBoundedAncestor;

public class EducationStoretypeNNSales extends StoretypeBoundedAncestor {

	@Override
	protected String[] getFields() {
		return new String[]{
				"store_type",
				"education_high_school_diploma", "education_some_college", 
				"education_bachelor_degree","education_graduate_degree","education_less_than_high_school","education_unknown"}; 
	}
}
