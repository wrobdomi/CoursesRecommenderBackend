package com.example.demo.servicelayer.impl;

import com.example.demo.datalayer.entity.CourseEntity;
import com.example.demo.datalayer.entity.RecommendationsEntity;
import com.example.demo.datalayer.entity.UserEntity;
import com.example.demo.datalayer.repository.CourseRepository;
import com.example.demo.datalayer.repository.RatingRepository;
import com.example.demo.datalayer.repository.RecommendationRepository;
import com.example.demo.datalayer.repository.UserRepoitory;
import com.example.demo.servicelayer.RecommendationsService;
import com.example.demo.servicelayer.UserService;
import com.example.demo.shared.dto.*;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationsServiceImpl implements RecommendationsService {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    UserRepoitory userRepoitory;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserService userService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    DataSource dataSource;

    @Autowired
    RecommendationRepository recommendationRepository;

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

        // 3. get number of ratings for each course, return Map<CourseId, NumOfRating>
        TypedQuery<CourseRatingCount> q2 = entityManager.createQuery(
                "SELECT NEW com.example.demo.shared.dto.CourseRatingCount(u.courseId, count(u.rating)) FROM user_course_ratings u GROUP BY u.courseId",
                CourseRatingCount.class);

        List<CourseRatingCount> courseRatingCounts = q2.getResultList();
        Map<Long, Long> ratingsCountsMap = new HashMap<>();
        for(CourseRatingCount crse: courseRatingCounts){
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


        // 6. Sort by damped mean
        Collections.sort(dampedMeans, Collections.reverseOrder());

        // 7. Take 3 best
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

    @Override
    public List<CourseDto> getCollaborativeRecommendations(String userId) {

        UserEntity userEntity = userRepoitory.findByUserId(userId);
        long id = userEntity.getId();

        DataModel dataModel = null;
        try {
            dataModel = new ReloadFromJDBCDataModel(new PostgreSQLJDBCDataModel(
                    dataSource, "user_course_ratings",
                    "user_id", "course_id",
                    "rating", null));
        } catch (TasteException e) {
            e.printStackTrace();
        }

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
            recommendations = recommender.recommend(id, 3);
        } catch (TasteException e) {
            e.printStackTrace();
        }

        List<Long> recommendedIds = new ArrayList<>();

        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);
            recommendedIds.add(recommendation.getItemID());
        }

        List<Long> ids = Arrays.asList(new Long[]{recommendedIds.get(0), recommendedIds.get(1), recommendedIds.get(2)});
        List<CourseEntity> courses = entityManager.createQuery("SELECT c FROM courses c WHERE c.id IN :ids").setParameter("ids", ids).getResultList();

        ModelMapper modelMapper = new ModelMapper();
        List<CourseDto> recommendedCourses = new ArrayList<>();
        for(CourseEntity ce: courses) {
            recommendedCourses.add(modelMapper.map(ce, CourseDto.class));
        }

        return recommendedCourses;
    }

    @Override
    public ContentRecommendationsDto getContentRecommendations(String userId) {

        long courseId = 0;

        List<RatingDto> ratingDtos = userService.getUsersRatings(userId);

        Collections.sort(ratingDtos, (a,b) -> {
            return (int) (b.getRating() - a.getRating());
        });
        System.out.println("RatingsDtos from best to worst: ");
        for(RatingDto ratingDto: ratingDtos){
            System.out.println(
                            "Course Id " + ratingDto.getCourseId() +
                            " user: " + ratingDto.getUserId() +
                            " rating: " + ratingDto.getRating());
        }

        List<RatingDto> betterThanThreeRatings = ratingDtos
                                                    .stream()
                                                    .filter(rDto -> rDto.getRating() > 3)
                                                    .collect(Collectors.toList());

        System.out.println("RatingsDtos better than 3: ");
        for(RatingDto ratingDto: betterThanThreeRatings){
            System.out.println(
                    "Course Id " + ratingDto.getCourseId() +
                            " user: " + ratingDto.getUserId() +
                            " rating: " + ratingDto.getRating());
        }

        CourseEntity courseEntity = null;

        if(betterThanThreeRatings.isEmpty()){
            courseEntity = this.courseRepository.findByCourseId(ratingDtos.get(0).getCourseId());
            courseId = courseEntity.getId();
        } else {
            Random r = new Random();
            int ranIndex = r.nextInt(betterThanThreeRatings.size());
            courseEntity = this.courseRepository.findByCourseId(betterThanThreeRatings.get(ranIndex).getCourseId());
            courseId = courseEntity.getId();
        }

        RecommendationsEntity re = recommendationRepository.findByCourseId(courseId);

        List<Long> ids = Arrays.asList(new Long[]{re.getrOne(), re.getrTwo(), re.getrThree()});
        List<CourseEntity> courses = entityManager.createQuery("SELECT c FROM courses c WHERE c.id IN :ids").setParameter("ids", ids).getResultList();


        ModelMapper modelMapper = new ModelMapper();
        List<CourseDto> recommendedCourses = new ArrayList<>();
        for(CourseEntity ce: courses) {
            recommendedCourses.add(modelMapper.map(ce, CourseDto.class));
        }

        ContentRecommendationsDto contentRecommendationsDto = new ContentRecommendationsDto();
        contentRecommendationsDto.setCourseDtoList(recommendedCourses);
        contentRecommendationsDto.setCourseName(courseEntity.getName());

        return contentRecommendationsDto;
    }


}
