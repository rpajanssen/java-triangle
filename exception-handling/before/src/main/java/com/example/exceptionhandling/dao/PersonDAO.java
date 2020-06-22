package com.example.exceptionhandling.dao;

import java.io.Serializable;
import java.util.List;

public interface PersonDAO<T> extends Serializable {
    List<T> findAll();
    T findById(long id);
    List<T> findWithLastName(String lastName);

    // NOTE : example 8 - Throwing generic exceptions
    T add(T person) throws Exception;
    // NOTE : example 8 - Throwing generic exceptions
    void update(T person) throws Exception;
    void delete(Long id);
    boolean existsById(Long id);
}
