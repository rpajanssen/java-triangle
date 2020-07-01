package com.example.exceptionhandling.repositories;

import com.example.exceptionhandling.domain.db.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Note that we extend JpaRepository and not CrudRepository. This is because we want our methods to return List instead
 * of Iterable type instances.
 */
@Repository
@Transactional
public interface PersonEntityRepository extends JpaRepository<PersonEntity, Long> {

    void deleteById(Long id);
}
