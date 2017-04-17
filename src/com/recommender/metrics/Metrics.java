package com.recommender.metrics;

public class Metrics {
	public static double meanAbsoluteError(double[] predictedRating, double[] realRating, int numberOfItems) {
		double sum = 0.0;
		for(int i = 0; i < numberOfItems; i++) {
			sum += Math.abs(predictedRating[i] - realRating[i]);
		}
		return sum/numberOfItems;	
	}
}
