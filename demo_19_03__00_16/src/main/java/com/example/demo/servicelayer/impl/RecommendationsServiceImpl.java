package com.example.demo.servicelayer.impl;

import com.example.demo.datalayer.repository.RatingRepository;
import com.example.demo.servicelayer.RecommendationsService;
import com.example.demo.shared.dto.CourseDto;
import com.example.demo.shared.dto.CourseRatingStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class RecommendationsServiceImpl implements RecommendationsService {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    public List<CourseDto> getNonPersonalizedRecommendations() {

        // 1. get average of all courses, return float
        double coursesAverage = ratingRepository.getAverageRatingForAllCourses();
        System.out.println("Courses avg is: " + coursesAverage);

        // 2. get sum for each course, return Map<CourseId, Sum>
        TypedQuery<CourseRatingStatistics> q = entityManager.createQuery(
                "SELECT NEW com.example.demo.shared.dto.CourseRatingStatistics(u.courseId, sum(u.rating)) FROM user_course_ratings u GROUP BY u.courseId",
                CourseRatingStatistics.class);

        List<CourseRatingStatistics> courseRatingSumEntities = q.getResultList();
        for(CourseRatingStatistics crse: courseRatingSumEntities){
            System.out.println("CourseId: " + crse.getCourse_id() + " ,sum: " + crse.getSum());
        }

        // 3. get number of ratings for each course, return Map<CourseId, NumOfRating>

        // 4. Choose k

        // 5. Process damped mean

        // 6. Sort by damped mean

        // 7. Take 3 best

        // Query db for three best

        // Return three best


        return null;
    }
}
