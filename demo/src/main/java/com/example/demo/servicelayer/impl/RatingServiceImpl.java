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

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    CourseService courseService;

    @Autowired
    UserService userService;

    @Autowired
    RatingRepository ratingRepository;

    @Override
    public List<RatingDto> createRating(List<RatingDto> ratingsDtoList) {


        List<RatingDto> newlyCreatedRatings = new ArrayList<>();

        UserDto userDto = userService.getUserByUserId(ratingsDtoList.get(0).getUserId());

        for(RatingDto rd : ratingsDtoList) {

            CourseDto courseDto = courseService.getCourseById(rd.getCourseId());

            courseService.updateCourseRating(courseDto.getCourseId(), rd.getRating());

            UserCourseRatingEntity userCourseRatingEntity = new UserCourseRatingEntity();

            userCourseRatingEntity.setCourseId(courseDto.getId());
            userCourseRatingEntity.setUserId(userDto.getId());
            userCourseRatingEntity.setRating(rd.getRating());

            ratingRepository.save(userCourseRatingEntity);

            RatingDto newRatingDto = new RatingDto();
            newRatingDto.setCourseId(rd.getCourseId());
            newRatingDto.setRating(rd.getRating());
            newRatingDto.setUserId(userDto.getUserId());

            newlyCreatedRatings.add(newRatingDto);
        }

        List<RatingDto> oldUsersRatings = userService.getUsersRatings(userDto.getUserId());

        newlyCreatedRatings.addAll(oldUsersRatings);

        return newlyCreatedRatings;
    }

}
