package com.example.demo.servicelayer.impl;

import com.example.demo.datalayer.entity.CourseEntity;
import com.example.demo.datalayer.repository.RatingRepository;
import com.example.demo.servicelayer.RecommendationsService;
import com.example.demo.shared.dto.CourseDto;
import com.example.demo.shared.dto.CourseRatingCount;
import com.example.demo.shared.dto.CourseRatingSum;
import com.example.demo.shared.dto.DampedMean;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

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
        TypedQuery<CourseRatingSum> q = entityManager.createQuery(
                "SELECT NEW com.example.demo.shared.dto.CourseRatingSum(u.courseId, sum(u.rating)) FROM user_course_ratings u GROUP BY u.courseId",
                CourseRatingSum.class);

        List<CourseRatingSum> courseRatingSums = q.getResultList();
//        for(CourseRatingSum crse: courseRatingSums){
//            System.out.println("CourseId: " + crse.getCourse_id() + " ,sum: " + crse.getSum());
//        }

        // 3. get number of ratings for each course, return Map<CourseId, NumOfRating>
        TypedQuery<CourseRatingCount> q2 = entityManager.createQuery(
                "SELECT NEW com.example.demo.shared.dto.CourseRatingCount(u.courseId, count(u.rating)) FROM user_course_ratings u GROUP BY u.courseId",
                CourseRatingCount.class);

        List<CourseRatingCount> courseRatingCounts = q2.getResultList();
        Map<Long, Long> ratingsCountsMap = new HashMap<>();
        for(CourseRatingCount crse: courseRatingCounts){
//            System.out.println("CourseId: " + crse.getCourse_id() + " ,count: " + crse.getCount());
            ratingsCountsMap.put(crse.getCourse_id(), crse.getCount());
        }

        // 4. Choose k
        int k = 2;

        // 5. Process damped mean
        List<DampedMean> dampedMeans = new LinkedList<>();
        for(CourseRatingSum crs: courseRatingSums){
            double dMean = (crs.getSum() + k*coursesAverage) / (ratingsCountsMap.get(crs.getCourse_id()) + k);
            dampedMeans.add(new DampedMean(crs.getCourse_id(), dMean));
        }

//        for(DampedMean dm : dampedMeans){
//            System.out.println("Course id: " + dm.getCourseId() + " damped mean: " + dm.getDampedMean());
//        }

        // 6. Sort by damped mean
        Collections.sort(dampedMeans, Collections.reverseOrder());

        // 7. Take 3 best
//        for(DampedMean dm : dampedMeans){
//            System.out.println("Course id: " + dm.getCourseId() + " damped mean: " + dm.getDampedMean());
//        }

        // Query db for three best
        List<Long> ids = Arrays.asList(new Long[]{dampedMeans.get(0).getCourseId(), dampedMeans.get(1).getCourseId(), dampedMeans.get(2).getCourseId()});
        List<CourseEntity> courses = entityManager.createQuery("SELECT c FROM courses c WHERE c.id IN :ids").setParameter("ids", ids).getResultList();


        ModelMapper modelMapper = new ModelMapper();
        List<CourseDto> recommendedCourses = new ArrayList<>();
        for(CourseEntity ce: courses) {
            recommendedCourses.add(modelMapper.map(ce, CourseDto.class));
        }

        return recommendedCourses;
    }


}
