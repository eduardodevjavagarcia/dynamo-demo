package com.example.dynamodemo.exceptions;

import com.example.dynamodemo.controllers.advice.Error;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = -3114332472503568118L;

    private final HttpStatus status;
    private final List<Error> errors;

    public BadRequestException(Error error) {
        this.status = HttpStatus.BAD_REQUEST;
        List<Error> errorList = new ArrayList<>();
        errorList.add(error);
        this.errors = errorList;
    }

    public BadRequestException(List<Error> errors) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errors = errors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<Error> getErrors() {
        return errors;
    }

}
