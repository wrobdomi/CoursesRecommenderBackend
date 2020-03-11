package com.example.demo.datalayer.repository;

import com.example.demo.datalayer.composite.keys.UserCourseRatingKey;
import com.example.demo.datalayer.entity.UserCourseRatingEntity;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<UserCourseRatingEntity, UserCourseRatingKey> {
}
