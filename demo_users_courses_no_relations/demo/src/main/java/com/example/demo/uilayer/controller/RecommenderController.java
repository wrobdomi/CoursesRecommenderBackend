package com.example.demo.uilayer.controller;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.List;


@RestController
@RequestMapping("recommenders") // http://localhost:8080/recommenders
public class RecommenderController {

    @Autowired
    DataSource dataSource;

    @GetMapping
    public String getRecommendations() {
        // System.out.println(dataSource);

        PostgreSQLJDBCDataModel dataModel = new PostgreSQLJDBCDataModel(dataSource);

        UserSimilarity userSimilarity = null;
        try {
            userSimilarity = new PearsonCorrelationSimilarity(dataModel);
        } catch (TasteException e) {
            e.printStackTrace();
        }
        UserNeighborhood userNeighborhood = new ThresholdUserNeighborhood(0.1, userSimilarity, dataModel);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
        List<RecommendedItem> recommendations = null;
        try {
            recommendations = recommender.recommend(2, 3);
        } catch (TasteException e) {
            e.printStackTrace();
        }
        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);
        }


        return "get recommenders was called";
    }

}