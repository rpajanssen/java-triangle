package com.example.exceptionhandling.rest.resources;

import com.example.exceptionhandling.dao.PersonDAO;
import com.example.exceptionhandling.dao.exceptions.PersonAlreadyExistsException;
import com.example.exceptionhandling.dao.exceptions.PersonNotFoundException;
import com.example.exceptionhandling.domain.api.Person;
import com.example.exceptionhandling.domain.api.SafeList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/person")
public class PersonCrudResource {
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

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updatePerson(@Valid @RequestBody Person person) throws PersonNotFoundException {
        personDao.update(person);
    }

    @DeleteMapping(path="/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable("personId") long personId) {
        personDao.delete(personId);
    }
}
