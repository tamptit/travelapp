package com.travel.dto;

import com.travel.entity.User;

public class UserDto {
    private String username;
    private String email;
    private String fullName;
    private boolean gender;
//    private String imgAvart;

    public UserDto() {
    }

    public UserDto(User user){
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.gender = user.isGender();
    }

    public UserDto(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

//    public String getImgAvart() {
//        return imgAvart;
//    }
//
//    public void setImgAvart(String imgAvart) {
//        this.imgAvart = imgAvart;
//    }
}
