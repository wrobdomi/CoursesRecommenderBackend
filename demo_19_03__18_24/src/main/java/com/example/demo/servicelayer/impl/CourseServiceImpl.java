package com.example.demo.servicelayer.impl;

import com.example.demo.datalayer.entity.CourseEntity;
import com.example.demo.datalayer.entity.UserCourseRatingEntity;
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
    public CourseDto getCourseByLongId(long id) {

        CourseDto courseDto = new CourseDto();

        CourseEntity courseEntity = courseRepository.findById(id);

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

    @Override
    public void delete(String id) {

        CourseEntity courseEntity = courseRepository.findByCourseId(id);

        if(courseEntity == null)
            throw new RuntimeException("Course with id " + id + " can not be deleted - course does not exist");

        courseRepository.delete(courseEntity);

    }

    @Override
    public CourseDto updateCourse(String id, CourseDto courseDto) {

        CourseEntity courseEntity = courseRepository.findByCourseId(id);

        if(courseEntity == null){
            throw new RuntimeException("Can not update course - course does not exists");
        }

        // update logic can be put here...

        CourseEntity updatedCourseEntity = courseRepository.save(courseEntity);

        ModelMapper modelMapper = new ModelMapper();

        CourseDto updatedCourseDto = modelMapper.map(updatedCourseEntity, CourseDto.class);

        return updatedCourseDto;

    }

    @Override
    public float updateCourseRating(String courseId, float rating) {

        System.out.println("Inside update Course Rating");

        CourseEntity courseEntity = courseRepository.findByCourseId(courseId);

        double length = ( (double) courseEntity.getCourseRatings().size()) + 1.0;

        System.out.println("Length is " + length);

        double currentSum = 0;
        for(UserCourseRatingEntity ucre : courseEntity.getCourseRatings()) {
            currentSum += ucre.getRating();
        }

        System.out.println("Current sum from db is " + currentSum);

        currentSum += rating;
        System.out.println("Current sum after adding new rating " + currentSum);

        float average = (float) (currentSum / length);

        System.out.println("Average after multiplying " + average);

        courseEntity.setAverageRating(average);

        courseRepository.save(courseEntity);

        return average;
    }

}
