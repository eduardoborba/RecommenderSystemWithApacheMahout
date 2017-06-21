package com.recommender.itemrecommender;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class EvaluateWithAdaptwebData {
	public static int kNeighbors;
	public static String fileName = "data/exp1.csv";
	
	public static void main(String[] args) throws IOException, TasteException {
		DataModel model = new FileDataModel(new File(fileName));
		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
			@Override
			public Recommender buildRecommender(DataModel model) throws TasteException {
				UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(kNeighbors, similarity, model);
				return new GenericUserBasedRecommender(model, neighborhood, similarity);
			}
		};
		
		RecommenderBuilder itemRecommenderBuilder = new RecommenderBuilder() {
			@Override
			public Recommender buildRecommender(DataModel model) throws TasteException {
				TanimotoCoefficientSimilarity similarity = new TanimotoCoefficientSimilarity(model);
				return new GenericItemBasedRecommender(model, similarity);
			}
		};
		
		for(kNeighbors = 2; kNeighbors <= 20; kNeighbors++) {
			for(int rep = 1; rep <= 5; rep++) {
				IRStatistics stats = evaluator.evaluate(
						 recommenderBuilder, null, model, null, 5,
						 0.0,
						 0.9);
				System.out.println("k="+kNeighbors + "\t" + stats.getPrecision() + "\t" + stats.getRecall() + "\t" + stats.getF1Measure());
			}
		}
		
		
		for(int rep = 1; rep <= 5; rep++) {
			IRStatistics stats = evaluator.evaluate(
					 itemRecommenderBuilder, null, model, null, 5,
					 0.0,
					 0.9);
			System.out.println("itemBased" + "\t" + stats.getPrecision() + "\t" + stats.getRecall() + "\t" + stats.getF1Measure());
		}

	}

}
