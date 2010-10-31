package com.cleveralgorithms.hearst;

public class MutableDouble {
	
	public double value;
	
	public MutableDouble() {
		
	}
	
	public MutableDouble(MutableDouble d) {
		value = d.value;
	}
	
	public MutableDouble(double value) 
	{
		this.value = value;
	}
	
	public void increment() {
		this.value += 1;
	}

	@Override
	public String toString() {
		return "MutableDouble [value=" + value + "]";
	}
	
	
}
