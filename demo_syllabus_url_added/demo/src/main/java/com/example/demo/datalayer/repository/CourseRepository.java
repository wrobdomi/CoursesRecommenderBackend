package com.example.demo.datalayer.repository;

import com.example.demo.datalayer.entity.CourseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CourseRepository extends PagingAndSortingRepository<CourseEntity, Long> {

    CourseEntity findByName(String name);
    CourseEntity findByCourseId(String courseId);
    Page<CourseEntity> findByCategory(String category, Pageable pageable);
    List<CourseEntity> findAllByCategory(String category);

}
