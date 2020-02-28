package com.travel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.*;
import java.util.Date;

public class UserForm {
    @Size(min = 8, max = 50, message = "Username length must be between 8 and 50")
    @NotEmpty(message = "Username is required")
    private String username;

    @Size(min = 8, max = 50)
    @NotEmpty(message = "Email is required")
    private String email;

    @Size(min = 6, max=50, message = "Password should be minimum of 6 characters")
    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Full name is required")
    private String fullName;

    @Past(message = "Date of birth is incorrect")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dOfB;

    @NotNull(message = "Sex is required")
    private boolean gender;

    public UserForm() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Date getdOfB() {
        return dOfB;
    }

    public void setdOfB(Date dOfB) {
        this.dOfB = dOfB;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}
