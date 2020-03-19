package com.example.demo.uilayer.controller;

import com.example.demo.datalayer.repository.RatingRepository;
import com.example.demo.servicelayer.RecommendationsService;
import com.example.demo.shared.dto.CourseDto;
import com.example.demo.uilayer.model.response.CourseRest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("recommenders") // http://localhost:8080/recommenders
public class RecommenderController {


    @Autowired
    RecommendationsService recommendationsService;

    @Autowired
    RatingRepository ratingRepository;


    @GetMapping(value="/nonperson")
    public List<CourseRest>  getNonPersonalizedRecommendations() {
//        System.out.println("Inside non personalized recommendations !");

        List<CourseRest> courses = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        List<CourseDto> courseDtos = this.recommendationsService.getNonPersonalizedRecommendations();

        for(CourseDto courseDto : courseDtos) {
            CourseRest courseRest =  modelMapper.map(courseDto, CourseRest.class);
            courses.add(courseRest);
        }

        return courses;
    }

    @GetMapping(path="/collaborative/{userId}")
    public List<CourseRest> getCollaborativeRecommendations(@PathVariable String userId) {

//        PostgreSQLJDBCDataModel dataModel = new PostgreSQLJDBCDataModel(
//                dataSource, "user_course_ratings",
//                "user_id", "course_id",
//                "rating", null);
        List<CourseRest> courses = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        List<CourseDto> courseDtos = this.recommendationsService.getCollaborativeRecommendations(userId);

        for(CourseDto courseDto : courseDtos) {
            CourseRest courseRest =  modelMapper.map(courseDto, CourseRest.class);
            courses.add(courseRest);
        }

        return courses;

    }



}
