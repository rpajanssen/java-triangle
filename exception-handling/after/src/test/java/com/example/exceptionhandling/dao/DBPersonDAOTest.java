package com.example.exceptionhandling.dao;

import com.example.exceptionhandling.AvailableProfiles;
import com.example.exceptionhandling.dao.exceptions.PersonAlreadyExistsException;
import com.example.exceptionhandling.dao.exceptions.PersonNotFoundException;
import com.example.exceptionhandling.domain.api.Person;
import com.example.exceptionhandling.domain.db.PersonEntity;
import com.example.exceptionhandling.repositories.PersonEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * This wired unit test demonstrates the use of DataJpaTest to slice a test. With this annotation only the JPA related
 * beans will be wired, and not the whole application. This results is a faster test and it can result in less
 * test configuration to be taken care of.
 *
 * There are drawbacks as well. One is that the DataJpaTest annotation does not play nice with DBRider, it does not
 * seem to execute the data-set setup logic for each test. And this results in a more complex test setup and less control
 * over the data!
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
})
@Transactional
@ActiveProfiles(AvailableProfiles.LOCAL)
class DBPersonDAOTest {
    private DBPersonDAO underTest;

    @Autowired
    private PersonEntityRepository repository;

    /**
     * Since we cannot use DBRider we need to initialize the DB in a before-method before each test.
     */
    @BeforeEach
    void setup() {
        repository.saveAll(Arrays.asList(persons()));

        underTest = new DBPersonDAO(repository);
    }

    /**
     * Since we cannot use DBRider we need to explicitly clean the DB ourselves after each test.
     */
    @AfterEach
    void teardown() {
        repository.findAll().forEach(
                personEntity -> repository.delete(personEntity)
        );
    }

    /**
     * Note that the assertions are a bit more cumbersome, non-fluent and since we can't control the full DB
     * setup, we do not control the value of the ID's!
     */
    @Test
    void shouldListAllPersons() {
        assertThat(underTest).isNotNull();

        List<Person> result = underTest.findAll();
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getFirstName()).isEqualTo("Jan");
        assertThat(result.get(0).getLastName()).isEqualTo("Janssen");
        assertThat(result.get(1).getFirstName()).isEqualTo("Pieter");
        assertThat(result.get(1).getLastName()).isEqualTo("Pietersen");
        assertThat(result.get(2).getFirstName()).isEqualTo("Erik");
        assertThat(result.get(2).getLastName()).isEqualTo("Eriksen");
    }

    @Test
    void shouldFindAPersonById() {
        assertThat(underTest).isNotNull();

        List<Person> allPersons = underTest.findAll();
        Person expectedResult = allPersons.get(0);

        Person result = underTest.findById(expectedResult.getId());
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(expectedResult.getFirstName());
        assertThat(result.getLastName()).isEqualTo(expectedResult.getLastName());
    }

    @Test
    void shouldAddAPerson() throws Exception {
        assertThat(underTest).isNotNull();

        underTest.add(new Person(null, "Katy", "Perry"));

        List<Person> result = underTest.findAll();
        assertThat(result).hasSize(4);
        assertThat(result.get(0).getFirstName()).isEqualTo("Jan");
        assertThat(result.get(0).getLastName()).isEqualTo("Janssen");
        assertThat(result.get(1).getFirstName()).isEqualTo("Pieter");
        assertThat(result.get(1).getLastName()).isEqualTo("Pietersen");
        assertThat(result.get(2).getFirstName()).isEqualTo("Erik");
        assertThat(result.get(2).getLastName()).isEqualTo("Eriksen");
        assertThat(result.get(3).getFirstName()).isEqualTo("Katy");
        assertThat(result.get(3).getLastName()).isEqualTo("Perry");
    }

    @Test
    void shouldNotAddAPersonThatAlreadyExists() throws Exception {
        assertThat(underTest).isNotNull();

        List<Person> allPersons = underTest.findAll();
        Person existingPerson = allPersons.get(1);

        assertThatExceptionOfType(PersonAlreadyExistsException.class)
                .isThrownBy(() -> {
                    underTest.add(existingPerson);
                }).withMessage("person already exists");
    }

    /**
     * Not that we need to find the person instance in the DB first before we can update it since we have no control
     * and therefore do not know the value of the ID.
     */
    @Test
    void shouldUpdateAPerson() throws Exception {
        assertThat(underTest).isNotNull();

        Optional<PersonEntity> persons = repository.findOne(Example.of(new PersonEntity(null, "Erik", "Eriksen")));
        PersonEntity personEntity = persons.get();

        Person person = new Person(personEntity);
        person.setLastName("Erikson");
        underTest.update(person);

        List<Person> result = underTest.findAll();
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getFirstName()).isEqualTo("Jan");
        assertThat(result.get(0).getLastName()).isEqualTo("Janssen");
        assertThat(result.get(1).getFirstName()).isEqualTo("Pieter");
        assertThat(result.get(1).getLastName()).isEqualTo("Pietersen");
        assertThat(result.get(2).getFirstName()).isEqualTo("Erik");
        assertThat(result.get(2).getLastName()).isEqualTo("Erikson");
    }

    @Test
    void shouldNotUpdateANonExistingPerson() throws Exception {
        assertThat(underTest).isNotNull();

        Person nonExistingPerson = new Person(666L, "Luke", "Skywalker");

        assertThatExceptionOfType(PersonNotFoundException.class)
                .isThrownBy(() -> {
                    underTest.update(nonExistingPerson);
                }).withMessage("404");
    }

    @Test
    void shouldDeleteAPerson() {
        assertThat(underTest).isNotNull();

        Optional<PersonEntity> persons = repository.findOne(Example.of(new PersonEntity(null, "Pieter", "Pietersen")));
        PersonEntity person = persons.get();
        underTest.delete(person.getId());

        List<Person> result = underTest.findAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("Jan");
        assertThat(result.get(0).getLastName()).isEqualTo("Janssen");
        assertThat(result.get(1).getFirstName()).isEqualTo("Erik");
        assertThat(result.get(1).getLastName()).isEqualTo("Eriksen");
    }

    private PersonEntity[] persons() {
        return new PersonEntity[] {
                new PersonEntity(1L, "Jan", "Janssen"),
                new PersonEntity(2L, "Pieter", "Pietersen"),
                new PersonEntity(3L, "Erik", "Eriksen")
        };
    }
}
