package com.travel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "plan")

public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required !!!")
    @Size(min=4,max=200,message = "Please use 4 to 200 letters")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Content is required !!!")
    @Column(name = "content")
    private String content;

    @Column(name = "active_user")
    private int activeUser;

    @Column(name = "follow_user")
    private int followUser;

    @Column(name = "start_day")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDay;

    @Column(name = "end_day")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDay;

    @NotBlank(message = "Status is required!!!")
    @Column(name = "status")
    private String startus;

    @Column(name = "created_day")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDay;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedDay() {
        return createdDay;
    }

    public void setCreatedDay(Date createdDay) {
        this.createdDay = createdDay;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
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

    public String getStartus() {
        return startus;
    }

    public void setStartus(String startus) {
        this.startus = startus;
    }

    public int getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(int activeUser) {
        this.activeUser = activeUser;
    }

    public int getFollowUser() {
        return followUser;
    }

    public void setFollowUser(int followUser) {
        this.followUser = followUser;
    }
}
