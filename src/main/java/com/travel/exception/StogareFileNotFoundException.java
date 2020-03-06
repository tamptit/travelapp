package com.travel.exception;

public class StogareFileNotFoundException extends BadRequestException {
    private static final long serialVersionUID = 1L;

    public StogareFileNotFoundException(String message) {
        super(message);
    }

    public StogareFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
