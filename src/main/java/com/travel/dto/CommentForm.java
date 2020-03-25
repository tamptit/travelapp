package com.travel.dto;

public class CommentForm {

    private String content;

    public CommentForm() {
    }

    public CommentForm( String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
