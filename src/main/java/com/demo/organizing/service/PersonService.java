package com.demo.organizing.service;

import com.demo.organizing.entities.Person;
import com.demo.organizing.entities.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PersonService {
    Person savePerson(Person person);
    Role saveRole(Role role);
    void addRoleToPerson(String username, String roleName);
    Person getPersonByUsername(String username);
    List<Person> getPersons();
    boolean deletePersonById(Long id);
    ResponseEntity<?> savePersonByEmail(Person person);
    ResponseEntity<?> confirmEmail(String confirmationToken);
}
