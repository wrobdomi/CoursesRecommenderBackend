package com.example.demo.shared.dto;


import java.io.Serializable;

public class UserDto implements Serializable {

    private static final long serialVersionUID = 7272575672419045558L;

    private long id;                    // from database
    private String userId;              // public user id
    private String email;               // user email
    private String password;            // unencrypted
    private String encryptedPassword;   // encrypted password

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
