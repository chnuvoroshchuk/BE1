package com.demo.organizing.repo;

import com.demo.organizing.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByUsername(String username);
    Person findByEmailIgnoreCase(String emailId);
    Boolean existsByEmail(String email);
}
