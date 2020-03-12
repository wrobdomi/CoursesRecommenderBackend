package com.example.demo.datalayer.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity(name="courses")
public class CourseEntity implements Serializable {

    private static final long serialVersionUID = 3916488172522780183L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String courseId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length=50)
    private String category;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable=false)
    private String syllabusUrl;

    @Column(nullable = false, length=50)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date publicationDate;

    @OneToMany(mappedBy = "courseEntity")
    Set<UserCourseRatingEntity> courseRatings;

    public Set<UserCourseRatingEntity> getCourseRatings() {
        return courseRatings;
    }

    public void setCourseRatings(Set<UserCourseRatingEntity> courseRatings) {
        this.courseRatings = courseRatings;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getSyllabusUrl() {
        return syllabusUrl;
    }

    public void setSyllabusUrl(String syllabusUrl) {
        this.syllabusUrl = syllabusUrl;
    }
}
