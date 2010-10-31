package com.cleveralgorithms.hearst;

public class Utils
{
	public final static double normalize(double min, double max, double value)
	{
		double range = max - min;
		if (range == 0) {
			value = 0;
		} else {
			value = (value - min) / range;	
		}
		 
		if(value>1 || value<0) {
			throw new RuntimeException("Normalization failed with: " + value);
		}
		return value;
	}
	
	public final static double[] normalize(double [][] minmax, double [] values)
	{
		double [] v = new double[values.length];
		
		for (int i = 0; i < v.length; i++) {
	                v[i] = normalize(minmax[i][0], minmax[i][1], values[i]);
                }
		
		return v;
	}
	
	public final static void normalizeInPlace(double [][] minmax, double [] values)
	{	
		for (int i = 0; i < values.length; i++) {
			values[i] = normalize(minmax[i][0], minmax[i][1], values[i]);
                }
	}
	
	public final static void normalizeInPlace(double [][] minmax, int [] ignore, double [] values)
	{	
		for (int i = 0; i < values.length; i++) {
			if (contains(ignore, i)) {
				continue;
			}
			values[i] = normalize(minmax[i][0], minmax[i][1], values[i]);
                }
	}
	
	public final static double euclideanDistance(double [] v1, double [] v2) {
		double sum = 0;
		for (int i = 0; i < v1.length; i++) {
			double diff = v1[i]-v2[i];
			sum += diff*diff;
		}
		return Math.sqrt(sum);
	}
	
	public final static double euclideanDistance(double [] v1, double [] v2, int [] ignore) {
		double sum = 0;
		for (int i = 0; i < v1.length; i++) {
			if (contains(ignore, i)) {
				continue;
			}
			double diff = v1[i]-v2[i];
			sum += diff*diff;
		}
		return Math.sqrt(sum);
	}
	
	public final static boolean contains(int [] v, int k){
		for (int i = 0; i < v.length; i++) {
	                if(v[i] == k) {
	                	return true;
	                }
                }
		return false;
	}
}
