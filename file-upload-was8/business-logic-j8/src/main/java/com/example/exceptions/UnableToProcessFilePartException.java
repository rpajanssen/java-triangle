package com.example.exceptions;

public class UnableToProcessFilePartException extends RuntimeException {
    public UnableToProcessFilePartException(Throwable throwable) {
        super(throwable);
    }
}
