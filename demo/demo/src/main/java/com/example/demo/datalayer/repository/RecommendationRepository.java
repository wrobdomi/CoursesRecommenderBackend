package com.example.demo.datalayer.repository;

import com.example.demo.datalayer.entity.RecommendationsEntity;
import org.springframework.data.repository.CrudRepository;

public interface RecommendationRepository extends CrudRepository<RecommendationsEntity, Long> {
    RecommendationsEntity findByCourseId(long courseId);
}
