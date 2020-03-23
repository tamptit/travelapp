package com.travel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.Schedule;
import com.travel.entity.User;

import java.util.Date;
import java.util.List;

public class PlanInteractorDto {
    private long id;
    private UserDto userDto;
    private Long idUser;
    private Long idPlan;
    private User user;
    private Plan plan;
    private PlanDto planDto;
    private boolean follow;
    private boolean join;


    public PlanInteractorDto() {
    }

    public PlanInteractorDto(User user, Plan plan) {
        this.user = user;
        this.plan = plan;
    }

    public PlanInteractorDto( Long userLogin, Long idPlan, boolean follow, boolean join){
        this.idUser = userLogin;
        this.idPlan = idPlan;
        this.follow = follow;
        this.join = join;
    }

    public PlanInteractorDto(long id, UserDto userDto,Long idPlan, boolean follow, boolean join) {
        this.id = id;
        this.userDto = userDto;
        this.idPlan = idPlan;
        this.follow = follow;
        this.join = join;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public boolean isJoin() {
        return join;
    }

    public void setJoin(boolean join) {
        this.join = join;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
}
