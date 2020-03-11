package com.example.demo.datalayer.entity;

import com.example.demo.datalayer.composite.keys.UserCourseRatingKey;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "user_course_ratings")
@IdClass(UserCourseRatingKey.class)
public class UserCourseRatingEntity implements Serializable {

    private static final long serialVersionUID = 126704079613374426L;

    @Id
    private long userId;

    @Id
    private long courseId;

    @Column(nullable = false)
    private double rating;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    UserEntity userEntity;

    @ManyToOne
    @MapsId("course_id")
    @JoinColumn(name = "course_id")
    CourseEntity courseEntity;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public CourseEntity getCourseEntity() {
        return courseEntity;
    }

    public void setCourseEntity(CourseEntity courseEntity) {
        this.courseEntity = courseEntity;
    }
}
