package com.travel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.dto.PlanInteractorDto;
import com.travel.dto.UserDto;
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
    private boolean follow;

    @Column(name = "joined")
    private boolean join;

    public PlanInteractor() {
    }

    public PlanInteractor(long id, @NotEmpty User user, @NotEmpty Plan plan) {
        this.id = id;
        this.user = user;
        this.plan = plan;
    }

    public PlanInteractor(User user, Plan plan, boolean follow, boolean join) {
        this.user = user;
        this.plan = plan;
        this.follow = follow;
        this.join = join;
    }

    public PlanInteractorDto convertToDto() {
        return new PlanInteractorDto(this.id, new UserDto(this.user), this.plan == null ? null : this.plan.getId(), this.follow, this.join );
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
}
