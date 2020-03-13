package com.travel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.entity.PlanInteractor;
import com.travel.entity.Schedule;
import com.travel.entity.User;

import java.util.Date;
import java.util.List;

public class PlanInteractorDto {
    private long id;
    private UserDto userDto;
    private int status;

    public PlanInteractorDto() {
    }

    public PlanInteractorDto(long id, User user, int status) {
        this.id = id;
        this.userDto = new UserDto(user);
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
