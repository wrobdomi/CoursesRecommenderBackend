package com.example.demo.servicelayer.impl;

import com.example.demo.datalayer.entity.CourseEntity;
import com.example.demo.datalayer.repository.CourseRepository;
import com.example.demo.servicelayer.CourseService;
import com.example.demo.shared.Utils;
import com.example.demo.shared.dto.CourseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    Utils utils;

    @Autowired
    CourseRepository courseRepository;


    @Override
    public CourseDto createCourse(CourseDto courseDto) {

        if(courseRepository.findByName(courseDto.getName()) != null )
            throw new RuntimeException("Course already exists");

        CourseEntity courseEntity = new CourseEntity();

        String publicCourseId = utils.generatePublicCourseId(10);
        Date publicationDate = new Date();

        ModelMapper modelMapper = new ModelMapper();
        courseEntity = modelMapper.map(courseDto, CourseEntity.class);

        courseEntity.setCourseId(publicCourseId);
        courseEntity.setPublicationDate(publicationDate);

        CourseEntity savedCourse = courseRepository.save(courseEntity);

        CourseDto returnValue = modelMapper.map(savedCourse, CourseDto.class);

        return  returnValue;

    }

    @Override
    public CourseDto getCourseById(String courseId) {

        CourseDto courseDto = new CourseDto();

        CourseEntity courseEntity = courseRepository.findByCourseId(courseId);

        if(courseEntity == null)
            throw new RuntimeException("Course with id " + courseId + " does not exists");

        ModelMapper modelMapper = new ModelMapper();

        courseDto = modelMapper.map(courseEntity, CourseDto.class);

        return courseDto;
    }

    @Override
    public List<CourseDto> getCourses(int page, int limit, String category, String sortBy) {

        if(page > 0) {
            page -= 1;
        }

        List<CourseDto> returnValue = new ArrayList<>();

        Pageable pageableRequest;

        if(sortBy.equals("none")){
            pageableRequest = PageRequest.of(page, limit);
        }
        else{
            pageableRequest = PageRequest.of(page, limit, Sort.by(sortBy));
        }


        Page<CourseEntity> coursesPage;

        if(category.equals("all")){
            coursesPage = courseRepository.findAll(pageableRequest);
        }
        else{
            coursesPage = courseRepository.findByCategory(category, pageableRequest);
        }

        List<CourseEntity> courseEntities = coursesPage.getContent();

        ModelMapper modelMapper = new ModelMapper();

        for(CourseEntity courseEntity : courseEntities) {
            CourseDto courseDto = modelMapper.map(courseEntity, CourseDto.class);
            returnValue.add(courseDto);
        }

        return returnValue;

    }

}