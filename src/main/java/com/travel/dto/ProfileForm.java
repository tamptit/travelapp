package com.travel.dto;

import com.travel.model.User;

import java.util.List;

public class ProfileForm {

    private User user; // model

    private  List<PlanProfileRespone> listJoinPlan;

    private  List<PlanProfileRespone> listFollowPlan;

    private List<PlanProfileRespone> listMyPlan;

    public ProfileForm() {
    }

    public ProfileForm(User user, List<PlanProfileRespone> listJoinPlan, List<PlanProfileRespone> listFollowPlan, List<PlanProfileRespone> listMyPlan) {
        this.user = user;
        this.listJoinPlan = listJoinPlan;
        this.listFollowPlan = listFollowPlan;
        this.listMyPlan = listMyPlan;
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

    public List<PlanProfileRespone> getListMyPlan() {
        return listMyPlan;
    }

    public void setListMyPlan(List<PlanProfileRespone> listMyPlan) {
        this.listMyPlan = listMyPlan;
    }
}
