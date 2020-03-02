package com.travel.dto;

import com.travel.entity.Plan;
import com.travel.entity.User;

import java.util.List;

public class UserPageResponse {
    private int currentPage;
    private int totalPage;
    private List<User> users;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
