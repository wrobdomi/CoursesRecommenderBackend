package com.example.demo.uilayer.controller;

import com.example.demo.servicelayer.RatingService;
import com.example.demo.shared.dto.RatingDto;
import com.example.demo.uilayer.model.request.RatingModel;
import com.example.demo.uilayer.model.response.OperationStatusModel;
import com.example.demo.uilayer.model.response.RequestOperationName;
import com.example.demo.uilayer.model.response.RequestOperationStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ratings")
public class RatingsController {

    @Autowired
    RatingService ratingService;

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
    public OperationStatusModel createRating(@RequestBody RatingModel ratingModel){

        OperationStatusModel operationStatusModel = new OperationStatusModel();

        operationStatusModel.setOperationName(RequestOperationName.POST.name());

        ModelMapper modelMapper = new ModelMapper();

        RatingDto ratingDto = modelMapper.map(ratingModel, RatingDto.class);

        ratingService.createRating(ratingDto);

        operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return  operationStatusModel;

    }

}
