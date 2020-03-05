package com.travel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

public class ProfileForm {

    private User user; // model

    private  List<Plan> joinPlan;

    private  List<Plan> followPlan;

    public ProfileForm() {
    }

    public ProfileForm(User user, List<Plan> joinPlan, List<Plan> flowPlan) {
        this.user = user;
        this.joinPlan = joinPlan;
        this.followPlan = flowPlan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Plan> getJoinPlan() {
        return joinPlan;
    }

    public void setJoinPlan(List<Plan> joinPlan) {
        this.joinPlan = joinPlan;
    }

    public List<Plan> getFlowPlan() {
        return followPlan;
    }

    public void setFlowPlan(List<Plan> flowPlan) {
        this.followPlan = flowPlan;
    }

}
