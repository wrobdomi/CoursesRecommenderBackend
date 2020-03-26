package com.example.demo.servicelayer;

import com.example.demo.shared.dto.ContentRecommendationsDto;
import com.example.demo.shared.dto.CourseDto;

import java.util.List;

public interface RecommendationsService {

    List<CourseDto> getNonPersonalizedRecommendations();

    List<CourseDto> getCollaborativeRecommendations(String userId);

    ContentRecommendationsDto getContentRecommendations(String userId);
}
