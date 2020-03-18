package com.example.demo.datalayer.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity(name = "users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -9045763698626805057L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable=false)
    private String userId;

    @Column(nullable=false, length=100)
    private String email;

    @Column(nullable=false)
    private String encryptedPassword;

    @OneToMany(mappedBy = "userEntity")
    Set<UserCourseRatingEntity> usersRatings;

    public Set<UserCourseRatingEntity> getUsersRatings() {
        return usersRatings;
    }

    public void setUsersRatings(Set<UserCourseRatingEntity> usersRatings) {
        this.usersRatings = usersRatings;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

}
