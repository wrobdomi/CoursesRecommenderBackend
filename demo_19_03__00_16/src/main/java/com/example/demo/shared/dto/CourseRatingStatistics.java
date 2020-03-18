package com.example.demo.shared.dto;

public class CourseRatingStatistics {

    private long course_id;
    private double sum;

    public CourseRatingStatistics(long course_id, double sum) {
        this.course_id = course_id;
        this.sum = sum;
    }

    public long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(long course_id) {
        this.course_id = course_id;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}

