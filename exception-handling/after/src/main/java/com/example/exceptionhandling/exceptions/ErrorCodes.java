package com.example.exceptionhandling.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorCodes {
    ACCESS_DENIED("0005", HttpStatus.FORBIDDEN),
    PERSON_NOT_FOUND("0010", HttpStatus.NOT_FOUND),
    PERSON_ALREADY_EXISTS("0015", HttpStatus.BAD_REQUEST),
    PERSON_INVALID("0020", HttpStatus.BAD_REQUEST),

    TOO_MANY_REQUESTS("0101", HttpStatus.TOO_MANY_REQUESTS),

    UNEXPECTED_EXCEPTION("9999", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final String code;
    private final HttpStatus httpStatus;

    ErrorCodes(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
