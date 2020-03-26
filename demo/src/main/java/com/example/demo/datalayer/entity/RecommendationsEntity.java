package com.example.demo.datalayer.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="recommendations")
public class RecommendationsEntity implements Serializable {

    private static final long serialVersionUID = 2598416225911577580L;

    @Id
    @Column(name="course_id")
    private long courseId;

    @Column
    private long rOne;

    @Column
    private long rTwo;

    @Column
    private long rThree;

    @OneToOne
    @MapsId("course_id")
    @JoinColumn(name = "course_id")
    CourseEntity courseEntity;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getrOne() {
        return rOne;
    }

    public void setrOne(long rOne) {
        this.rOne = rOne;
    }

    public long getrTwo() {
        return rTwo;
    }

    public void setrTwo(long rTwo) {
        this.rTwo = rTwo;
    }

    public long getrThree() {
        return rThree;
    }

    public void setrThree(long rThree) {
        this.rThree = rThree;
    }
}
