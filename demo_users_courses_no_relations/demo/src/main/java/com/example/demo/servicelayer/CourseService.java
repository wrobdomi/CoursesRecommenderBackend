package com.example.demo.servicelayer;

import com.example.demo.shared.dto.CourseDto;

import java.util.List;

public interface CourseService {

    CourseDto createCourse(CourseDto courseDto);
    CourseDto getCourseById(String courseId);
    List<CourseDto> getCourses(int page, int limit, String category, String sortBy);

}
