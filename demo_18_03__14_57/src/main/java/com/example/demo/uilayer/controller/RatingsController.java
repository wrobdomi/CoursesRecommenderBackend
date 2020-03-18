package com.example.demo.uilayer.controller;

import com.example.demo.servicelayer.RatingService;
import com.example.demo.servicelayer.UserService;
import com.example.demo.shared.dto.RatingDto;
import com.example.demo.uilayer.model.request.RatingModel;
import com.example.demo.uilayer.model.request.RatingsHolderModel;
import com.example.demo.uilayer.model.response.RatingRest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("ratings")
public class RatingsController {

    @Autowired
    RatingService ratingService;

    @Autowired
    UserService userService;

    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
        )
    public List<RatingRest> createRating(@RequestBody RatingsHolderModel ratings){

        List<RatingRest> returnValue = new ArrayList<>();

        List<RatingDto> dtoRatings = new ArrayList<>();

        ModelMapper modelMapper = new ModelMapper();

        for(RatingModel rm : ratings.getRatings()){
            dtoRatings.add(modelMapper.map(rm, RatingDto.class));
        }

        List<RatingDto> usersRatings = ratingService.createRating(dtoRatings);

        for(RatingDto ratingDto : usersRatings) {
            returnValue.add(modelMapper.map(ratingDto, RatingRest.class));
        }

        return returnValue;

    }

}
