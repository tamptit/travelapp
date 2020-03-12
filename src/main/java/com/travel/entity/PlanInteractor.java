package com.travel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.model.AuditModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "plan_interactor")
public class PlanInteractor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@NotEmpty
    @ManyToOne
    @JsonIgnore
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    //@NotEmpty
    @ManyToOne
    @JsonIgnore
    @JoinColumn(nullable = false, name = "plan_id")
    private Plan plan;

    @Column(name = "follow")
    private int follow;

    @Column(name = "joined")
    private int join;

    public PlanInteractor() {
    }

    public PlanInteractor(long id, @NotEmpty User user, @NotEmpty Plan plan) {
        this.id = id;
        this.user = user;
        this.plan = plan;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getJoin() {
        return join;
    }

    public void setJoin(int join) {
        this.join = join;
    }
}
