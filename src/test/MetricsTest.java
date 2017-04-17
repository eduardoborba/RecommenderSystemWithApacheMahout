package test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.recommender.metrics.Metrics;

public class MetricsTest {

	@Test
	public void meanAbsoluteErrorMustCalculateCorrectly() {
		int numberOfItems = 10;
		
		double[] predictedRating = {4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0};
		double[] realRating = {3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0};
		double meanAbsoluteError = Metrics.meanAbsoluteError(predictedRating, realRating, numberOfItems);
		assertEquals(meanAbsoluteError, 1.0, 0.001);
		
		double[] predictedRating2 = {4.0, 3.0, 4.0, 3.0, 4.0, 3.0, 4.0, 3.0, 4.0, 3.0};
		meanAbsoluteError = Metrics.meanAbsoluteError(predictedRating2, realRating, numberOfItems);
		assertEquals(meanAbsoluteError, 0.5, 0.001);
		
		double[] realRating2 = {2.0, 3.0, 2.0, 3.0, 2.0, 3.0, 2.0, 3.0, 2.0, 3.0};
		meanAbsoluteError = Metrics.meanAbsoluteError(predictedRating, realRating2, numberOfItems);
		assertEquals(meanAbsoluteError, 1.5, 0.001);
	}

}
