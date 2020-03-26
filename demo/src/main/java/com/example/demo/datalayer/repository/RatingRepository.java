package com.example.demo.datalayer.repository;

import com.example.demo.datalayer.composite.keys.UserCourseRatingKey;
import com.example.demo.datalayer.entity.UserCourseRatingEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<UserCourseRatingEntity, UserCourseRatingKey> {


    @Query(value = "SELECT avg(rating) FROM user_course_ratings", nativeQuery = true)
    double getAverageRatingForAllCourses();

//    @Query("SELECT NEW com.example.demo.shared.dto.CourseRatingSum(u.course_id, sum(u.rating)) FROM user_course_ratings u GROUP BY u.course_id")
//    List<CourseRatingSum> getSumForEachCourse();
}


