package com.travel.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class UserForm {
    @Size(min = 8, max = 50, message = "Username length must be between 8 and 50")
    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Email is required")
    @Size(min = 3, max = 100)
    private String email;

    @Size(min = 1, max = 6, message = "Size must be between 1 and 6")
    private String password;

    private String fullName;

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
