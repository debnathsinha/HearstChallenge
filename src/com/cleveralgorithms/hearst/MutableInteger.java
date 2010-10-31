package com.cleveralgorithms.hearst;

public class MutableInteger {
	
	public long value;
	
	public MutableInteger() {
		
	}
	
	public MutableInteger(MutableInteger d) {
		value = d.value;
	}
	
	public MutableInteger(long value) 
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
