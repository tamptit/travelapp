package com.travel.dto;

import javax.persistence.Entity;


public class ErrorMessage {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorMessage(String message) {
        this.message = message;
    }


}
