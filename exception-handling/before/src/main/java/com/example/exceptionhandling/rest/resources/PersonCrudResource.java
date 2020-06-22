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

// NOTE : Observe all the boilerplate exception handling code, it is verbose, it has duplications it distracts from
//        the actual code and makes the code less readable!
//        And... it'll probably is duplicated in each resource!
@RestController
@RequestMapping("/api/person")
public class PersonCrudResource {
    private static final Logger logger = LoggerFactory.getLogger(PersonCrudResource.class);

    private final PersonDAO<Person> personDao;

    public PersonCrudResource(PersonDAO<Person> personDao) {
        this.personDao = personDao;
    }

    // NOTE : example 7/8/10/11 - Catching/throwing very abstract generic exceptions, unnecessary exception chaining and
    //       cCatching and rethrowing the same exceptions
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SafeList<Person> allPersons() throws Exception {
        try {
            return new SafeList<>(personDao.findAll());
        } catch (Exception exception) {
            logger.error("unable to find all persons");
            throw new Exception(exception.getMessage(), exception);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person addPerson(@Valid @RequestBody Person person) throws PersonAlreadyExistsException {
        return personDao.add(person);
    }

    // NOTE : example 6 - Using low level code at wrong higher abstraction level
    // NOTE : example 11 - Unnecessary exception chaining
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

    // NOTE : example 14 - Unwanted swallowing of exceptions
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
