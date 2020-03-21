package com.example.demo.servicelayer;

import com.example.demo.shared.dto.RatingDto;

import java.util.List;

public interface RatingService  {

    List<RatingDto> createRating(List<RatingDto> ratingDto);

}
