package com.example.dynamodemo.exceptions;

public class StudentAlreadyExistException extends UnprocessableEntityException {

    private static final long serialVersionUID = 5620486342251934699L;

    public StudentAlreadyExistException() {
        super("422.001");
    }
}
