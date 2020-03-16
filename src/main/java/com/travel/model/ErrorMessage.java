package com.travel.model;

import org.springframework.http.HttpStatus;

public class ErrorMessage {
    private Enum code;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorMessage(HttpStatus badRequest, String message) {
        this.message = message;
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    public Enum getCode() {
        return code;
    }

    public void setCode(Enum code) {
        this.code = code;
    }
}
