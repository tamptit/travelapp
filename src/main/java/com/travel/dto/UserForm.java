package com.travel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class UserForm {

  private String username;
  private String email;
  private String password;
  private String fullName;
  private Date dOfB;
  private boolean gender;

  public UserForm() {}

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
