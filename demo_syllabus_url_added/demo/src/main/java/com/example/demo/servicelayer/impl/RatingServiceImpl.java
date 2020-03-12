package com.example.demo.servicelayer.impl;

import com.example.demo.datalayer.entity.UserCourseRatingEntity;
import com.example.demo.datalayer.repository.RatingRepository;
import com.example.demo.servicelayer.CourseService;
import com.example.demo.servicelayer.RatingService;
import com.example.demo.servicelayer.UserService;
import com.example.demo.shared.dto.CourseDto;
import com.example.demo.shared.dto.RatingDto;
import com.example.demo.shared.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    CourseService courseService;

    @Autowired
    UserService userService;

    @Autowired
    RatingRepository ratingRepository;

    @Override
    public RatingDto createRating(RatingDto ratingDto) {

        UserDto userDto = userService.getUserByUserId(ratingDto.getUserId());

        CourseDto courseDto = courseService.getCourseById(ratingDto.getCourseId());

        UserCourseRatingEntity userCourseRatingEntity = new UserCourseRatingEntity();

        userCourseRatingEntity.setCourseId(courseDto.getId());
        userCourseRatingEntity.setUserId(userDto.getId());
        userCourseRatingEntity.setRating(ratingDto.getRating());

        UserCourseRatingEntity savedRating = ratingRepository.save(userCourseRatingEntity);

        return ratingDto;
    }

}
