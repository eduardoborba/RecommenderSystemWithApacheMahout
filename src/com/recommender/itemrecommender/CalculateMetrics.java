package com.recommender.itemrecommender;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;

import com.recommender.metrics.Metrics;

public class CalculateMetrics {

	public static void main(String[] args) {
		try {
			int numberOfItems = 9430;
			double[] predictedRatings = new double[numberOfItems];
			double[] realRatings = new double[numberOfItems];
			
			DataModel dmBase = new FileDataModel(new File("data/a_base.csv"));
			DataModel dmTest = new FileDataModel(new File("data/a_test.csv"));
			
			Recommender recommender = initializeRecommender(dmBase);
			
			int i = 0;
			for(LongPrimitiveIterator users = dmTest.getUserIDs(); users.hasNext();) {
				long userId = users.next();
				
				for(LongPrimitiveIterator testItems = dmTest.getItemIDsFromUser(userId).iterator(); testItems.hasNext();) {
					long itemId = testItems.next();
					predictedRatings[i] = recommender.estimatePreference(userId, itemId);
					realRatings[i] = dmTest.getPreferenceValue(userId, itemId);
					if(Double.isNaN(predictedRatings[i])) {
						System.out.println("predicted NaN - userId:" + userId + " - itemId:" + itemId + " - predictedRating:" + predictedRatings[i] + " - realRating:" + realRatings[i]);
						continue;
					}
					i++;
				}
			}
			
			double meanAbsoluteError = Metrics.meanAbsoluteError(predictedRatings, realRatings, i);
			System.out.println("mean absolute error:" + meanAbsoluteError);
			System.out.println("number of unpredicted items:" + (numberOfItems - i));
		} catch (IOException e) {
			System.out.println("There was an error");
			e.printStackTrace();
		} catch (TasteException e) {
			System.out.println("There was an Taste error");
			e.printStackTrace();
		}

	}
	
	public static Recommender initializeRecommender(DataModel dm) throws TasteException {
		return initializeItemBasedRecommender(dm);
		// return initializeUserBasedRecommender(dm);
	}
	
	public static Recommender initializeItemBasedRecommender(DataModel dm) throws TasteException {
		ItemSimilarity sim = new LogLikelihoodSimilarity(dm);
		
		return new GenericItemBasedRecommender(dm, sim);
	}

	public static Recommender initializeUserBasedRecommender(DataModel dm) throws TasteException {
		UserSimilarity sim = new PearsonCorrelationSimilarity(dm);
		NearestNUserNeighborhood neighborhood = new NearestNUserNeighborhood(10, sim, dm);
		
		return new GenericUserBasedRecommender(dm, neighborhood, sim);
	}
}
