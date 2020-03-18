package com.example.demo.uilayer.controller;

import com.example.demo.servicelayer.CourseService;
import com.example.demo.shared.dto.CourseDto;
import com.example.demo.uilayer.model.request.CourseDetailsModel;
import com.example.demo.uilayer.model.response.CourseRest;
import com.example.demo.uilayer.model.response.OperationStatusModel;
import com.example.demo.uilayer.model.response.RequestOperationName;
import com.example.demo.uilayer.model.response.RequestOperationStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("courses")
public class CourseController { // http://localhost:8080/courses

    @Autowired
    CourseService courseService;

    @GetMapping(
            path="/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public CourseRest getCourse(@PathVariable String id){
        CourseRest courseRest = new CourseRest();

        CourseDto courseDto = courseService.getCourseById(id);

        ModelMapper modelMapper = new ModelMapper();

        courseRest = modelMapper.map(courseDto, CourseRest.class);

        return courseRest;
    }


    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public CourseRest createCourse(@RequestBody CourseDetailsModel courseDetailsModel) {

        CourseRest returnedValue = new CourseRest();

        CourseDto courseDto = new CourseDto();

        ModelMapper modelMapper = new ModelMapper();

        courseDto = modelMapper.map(courseDetailsModel, CourseDto.class);

        CourseDto createdCourse = courseService.createCourse(courseDto);

        returnedValue = modelMapper.map(createdCourse, CourseRest.class);

        return  returnedValue;

    }

    @GetMapping(
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public List<CourseRest> getCourses(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "60") int limit,
            @RequestParam(value = "category", defaultValue = "all") String category,
            @RequestParam(value = "sortBy", defaultValue = "none") String sortBy
    ) {

        List<CourseRest> courses = new ArrayList<>();

        List<CourseDto> courseDtos = courseService.getCourses(page, limit, category, sortBy);

        ModelMapper modelMapper = new ModelMapper();

        for(CourseDto courseDto : courseDtos) {
            CourseRest courseRest =  modelMapper.map(courseDto, CourseRest.class);
            courses.add(courseRest);
        }

        return courses;

    }

    @DeleteMapping(  path="/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public OperationStatusModel deleteCourse(@PathVariable String id){

        OperationStatusModel operationStatusModel = new OperationStatusModel();

        operationStatusModel.setOperationName(RequestOperationName.DELETE.name());

        courseService.delete(id);

        operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return  operationStatusModel;

    }

    @PutMapping(  path="/{id}",
            produces = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE
            },
            consumes = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE
            }
    )
    public CourseRest updateCourse(
            @PathVariable String id,
            @RequestBody CourseDetailsModel courseDetailsModel){

        ModelMapper modelMapper = new ModelMapper();

        CourseDto courseDto = modelMapper.map(courseDetailsModel, CourseDto.class);

        CourseDto updatedCourseDto = courseService.updateCourse(id, courseDto);

        CourseRest courseRest = modelMapper.map(updatedCourseDto, CourseRest.class);

        return  courseRest;
    }


}
