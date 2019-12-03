package com.example.dynamodemo.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8161151067069989136L;

    private final HttpStatus status;

    public NotFoundException() {
        this.status = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
