package com.travel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.Schedule;
import com.travel.entity.User;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PlanDto {
    private long id;

    private String name;

    private String content;

    private int status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDay;

    private String imageCover;

    private UserDto user;

    private List<PlanInteractorDto> planInteractorDtos;

    public PlanDto() {
    }

    public PlanDto(Plan plan, User user, List<PlanInteractor> planInteractors) {
        this.id = plan.getId();
        this.name = plan.getName();
        this.content = plan.getContent();
        this.status = plan.getStatus();
        this.imageCover = plan.getImageCover();
        this.user = new UserDto(user);
        this.planInteractorDtos = planInteractors.stream().map(PlanInteractor::convertToDto).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedDay() {
        return createdDay;
    }

    public void setCreatedDay(Date createdDay) {
        this.createdDay = createdDay;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<PlanInteractorDto> getPlanInteractorDtos() {
        return planInteractorDtos;
    }

    public void setPlanInteractorDtos(List<PlanInteractorDto> planInteractorDtos) {
        this.planInteractorDtos = planInteractorDtos;
    }
}
