package com.example.demo.shared.dto;

public class CourseRatingCount {

    private long course_id;
    private long count;

    public CourseRatingCount(long course_id, long count) {
        this.course_id = course_id;
        this.count = count;
    }

    public long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(long course_id) {
        this.course_id = course_id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
