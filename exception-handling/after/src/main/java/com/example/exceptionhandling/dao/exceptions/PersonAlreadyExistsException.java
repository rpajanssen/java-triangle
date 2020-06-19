package com.example.exceptionhandling.dao.exceptions;

import com.example.exceptionhandling.domain.api.Person;

public class PersonAlreadyExistsException extends Exception {
    private final Person person;

    public PersonAlreadyExistsException(Person person, final String message) {
        super((message));
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
}
