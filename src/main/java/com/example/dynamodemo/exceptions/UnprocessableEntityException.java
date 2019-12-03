package com.example.dynamodemo.exceptions;

import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends RuntimeException {

	private static final long serialVersionUID = -4629390046600317414L;

	private final HttpStatus status;
	private final String code;
	private final Object[] args;

	public UnprocessableEntityException(String code, Object... args) {
		this.status = HttpStatus.UNPROCESSABLE_ENTITY;
		this.code = code;
		this.args = args;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public Object[] getArgs() {
		return args;
	}
}