package com.example.demo.shared.dto;

import java.util.List;

public class ContentRecommendationsDto {

    private String courseName;
    private List<CourseDto> courseDtoList;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<CourseDto> getCourseDtoList() {
        return courseDtoList;
    }

    public void setCourseDtoList(List<CourseDto> courseDtoList) {
        this.courseDtoList = courseDtoList;
    }
}
