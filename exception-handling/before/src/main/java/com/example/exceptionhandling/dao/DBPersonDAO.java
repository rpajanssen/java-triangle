package com.example.exceptionhandling.dao;

import com.example.exceptionhandling.domain.api.Person;
import com.example.exceptionhandling.domain.db.PersonEntity;
import com.example.exceptionhandling.repositories.PersonEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DBPersonDAO implements PersonDAO<Person>{
    private static final Logger logger = LoggerFactory.getLogger(DBPersonDAO.class);

    private final PersonEntityRepository personRepository;

    public DBPersonDAO(PersonEntityRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> findAll() {
        return Optional.ofNullable(personRepository.findAll()).orElse(new ArrayList<>()).stream()
                .map(Person::new)
                .collect(Collectors.toList());
    }

    // todo : cleanup - never return null!
    @Override
    public Person findById(long id) {
        Optional<PersonEntity> optionalPersonEntity = personRepository.findById(id);

        if(optionalPersonEntity.isPresent()) {
            return new Person(optionalPersonEntity.get());
        }

        return null;
    }

    @Override
    public List<Person> findWithLastName(String lastName) {
        return null;
    }

    // NOTE : example 8 - Throwing generic exceptions
    @Override
    public Person add(Person person) throws Exception {
        if(existsById(person.getId())) {
            throw new Exception("person already exists");
        }
        return new Person(personRepository.save(new PersonEntity(person)));
    }

    // NOTE : example 5 - Using high level HTTP code at wrong abstraction level
    // NOTE : example 8 - Throwing generic exceptions
    @Override
    public void update(Person person) throws Exception {
        if(!existsById(person.getId())) {
            throw new Exception(String.valueOf(HttpStatus.NOT_FOUND.value()));
        }

        personRepository.save(new PersonEntity(person));
    }

    @Override
    public void delete(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return id != null && personRepository.existsById(id);
    }
}
