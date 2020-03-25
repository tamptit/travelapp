package com.travel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.dto.CommentDto;
import com.travel.dto.UserDto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(nullable = false, name = "plan_id")
    private Plan plan;

    @Column(name = "time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    @Column(name = "status")
    private  String status;

    public Comment() {
    }

    public Comment(String content, User user, Plan plan, Date time) {
        this.content = content;
        this.user = user;
        this.plan = plan;
        this.time = time;
    }

    public CommentDto convertCommentToDto(){
        return new CommentDto(new UserDto(this.user) ,this.content, this.time );
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

}
