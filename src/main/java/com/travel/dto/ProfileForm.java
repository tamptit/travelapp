package com.travel.dto;

import com.travel.model.User;

import java.util.List;

public class ProfileForm {

    private User user; // model

    private  List<PlanProfileRespone> listJoinPlan;

    private  List<PlanProfileRespone> listFollowPlan;

    public ProfileForm() {
    }

    public ProfileForm(User user, List<PlanProfileRespone> joinPlan, List<PlanProfileRespone> flowPlan) {
        this.user = user;
        this.listJoinPlan = joinPlan;
        this.listFollowPlan = flowPlan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PlanProfileRespone> getListJoinPlan() {
        return listJoinPlan;
    }

    public void setListJoinPlan(List<PlanProfileRespone> listJoinPlan) {
        this.listJoinPlan = listJoinPlan;
    }

    public List<PlanProfileRespone> getListFollowPlan() {
        return listFollowPlan;
    }

    public void setListFollowPlan(List<PlanProfileRespone> listFollowPlan) {
        this.listFollowPlan = listFollowPlan;
    }
}
