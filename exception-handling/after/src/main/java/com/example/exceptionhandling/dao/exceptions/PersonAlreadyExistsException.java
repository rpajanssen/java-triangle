package com.example.exceptionhandling.dao.exceptions;

import com.example.exceptionhandling.domain.api.Person;
import com.example.exceptionhandling.exceptions.CodedException;
import com.example.exceptionhandling.exceptions.ErrorCodes;

public class PersonAlreadyExistsException extends Exception implements CodedException {
    public static final ErrorCodes ERROR_CODE = ErrorCodes.PERSON_ALREADY_EXISTS;

    private final Person person;

    public PersonAlreadyExistsException(Person person, final String message) {
        super((message));
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public ErrorCodes getErrorCode() {
        return ERROR_CODE;
    }
}
