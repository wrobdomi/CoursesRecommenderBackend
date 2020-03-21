package com.example.demo.shared.dto;

public class DampedMean implements Comparable<DampedMean>{

    private Long courseId;
    private double dampedMean;

    public DampedMean(Long courseId, double dampedMean) {
        this.courseId = courseId;
        this.dampedMean = dampedMean;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public double getDampedMean() {
        return dampedMean;
    }

    public void setDampedMean(double dampedMean) {
        this.dampedMean = dampedMean;
    }

    @Override
    public int compareTo(DampedMean o) {
        if(this.getDampedMean() > o.getDampedMean()){
            return 1;
        }
        if(this.getDampedMean() < o.getDampedMean()){
            return -1;
        }
        return 0;
    }
}
