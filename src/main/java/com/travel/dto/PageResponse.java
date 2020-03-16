package com.travel.dto;

import com.travel.entity.Plan;

import java.util.List;

public class PageResponse<T> {
    private int currentPage;
    private int totalPage;
    private List<T> t;

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

    public List<T> getT() {
        return t;
    }

    public void setT(List<T> t) {
        this.t = t;
    }

    public List<T> getTs() {
        return t;
    }

    public void setPlans(List<T> ts) {
        this.t = ts;
    }
}
