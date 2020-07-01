package com.example.exceptionhandling.dao;

import com.example.exceptionhandling.dao.exceptions.PersonAlreadyExistsException;
import com.example.exceptionhandling.dao.exceptions.PersonNotFoundException;
import com.example.exceptionhandling.domain.api.Person;
import com.example.exceptionhandling.domain.db.PersonEntity;
import com.example.exceptionhandling.repositories.PersonEntityRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DBPersonDAO implements PersonDAO<Person>{
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
    public Person add(Person person) throws PersonAlreadyExistsException {
        if(existsById(person.getId())) {
            throw new PersonAlreadyExistsException(person, "person already exists");
        }
        return new Person(personRepository.save(new PersonEntity(person)));
    }

    @Override
    public void update(Person person) throws PersonNotFoundException {
        if(!existsById(person.getId())) {
            throw new PersonNotFoundException(person, "person does not exit");
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
