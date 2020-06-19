package com.example.exceptionhandling.dao;

import com.example.exceptionhandling.dao.exceptions.PersonAlreadyExistsException;
import com.example.exceptionhandling.dao.exceptions.PersonNotFoundException;

import java.io.Serializable;
import java.util.List;

public interface PersonDAO<T> extends Serializable {
    List<T> findAll();
    T findById(long id);
    List<T> findWithLastName(String lastName);

    T add(T person) throws PersonAlreadyExistsException;
    void update(T person) throws PersonNotFoundException;
    void delete(Long id);
    boolean existsById(Long id);
}
