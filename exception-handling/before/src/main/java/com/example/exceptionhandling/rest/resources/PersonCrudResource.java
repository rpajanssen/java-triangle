package com.example.exceptionhandling.rest.resources;

import com.example.exceptionhandling.dao.PersonDAO;
import com.example.exceptionhandling.dao.exceptions.PersonAlreadyExistsException;
import com.example.exceptionhandling.dao.exceptions.PersonNotFoundException;
import com.example.exceptionhandling.domain.api.Person;
import com.example.exceptionhandling.domain.api.SafeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/person")
public class PersonCrudResource {
    private final static Logger logger = LoggerFactory.getLogger(PersonCrudResource.class);

    private final PersonDAO<Person> personDao;

    public PersonCrudResource(PersonDAO<Person> personDao) {
        this.personDao = personDao;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SafeList<Person> allPersons() {
        return new SafeList<>(personDao.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person addPerson(@Valid @RequestBody Person person) throws PersonAlreadyExistsException {
        return personDao.add(person);
    }

    // NOTE : example 6 - Using low level code at wrong higher abstraction level
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updatePerson(@Valid @RequestBody Person person) throws Exception {
        try {
            personDao.update(person);
        } catch(PersonNotFoundException exception) {
            logger.error("unable to update person " + person.getId());
            throw new Exception(exception.getMessage(), exception);
        } catch(PersistenceException exception) {
            logger.error("unable to update person " + person.getId());
            throw new Exception(exception.getMessage(), exception);
        }
    }

    // NOTE : example 13 - Unwanted swallowing of exceptions
    @DeleteMapping(path="/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable("personId") long personId) {
        try {
            personDao.delete(personId);
        } catch (Exception e) {
            logger.error("unable to delete person " + personId);
        }
    }
}
