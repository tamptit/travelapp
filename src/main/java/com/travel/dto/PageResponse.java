package com.travel.dto;

import com.travel.entity.Plan;

import java.util.List;

public class PageResponse<T> {
    private int currentPage;
    private int totalPage;
    private List<T> content;

    public PageResponse() {
    }

    public PageResponse(int currentPage, int totalPage, List<T> content) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.content = content;
    }

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

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
