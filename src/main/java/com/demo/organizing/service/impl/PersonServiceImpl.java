package com.demo.organizing.service.impl;

import com.demo.organizing.entities.ConfirmationToken;
import com.demo.organizing.entities.Person;
import com.demo.organizing.entities.Role;
import com.demo.organizing.repo.ConfirmationTokenRepository;
import com.demo.organizing.repo.PersonRepository;
import com.demo.organizing.repo.RoleRepository;
import com.demo.organizing.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PersonServiceImpl implements PersonService, UserDetailsService {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    @Override
    public Person savePerson(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        log.info("Saving new person with username {} to the database", person.getUsername());
        return personRepository.save(person);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role with name {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToPerson(String username, String roleName) {
        log.info("Adding role {} to person {}", roleName, username);
        Person person = personRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        person.getRoles().add(role);
    }

    @Override
    public Person getPersonByUsername(String username) {
        log.info("Fetching person {}", username);
        return personRepository.findByUsername(username);
    }

    @Override
    public List<Person> getPersons() {
        log.info("Fetching all persons");
        return personRepository.findAll();
    }

    @Override
    public boolean deletePersonById(Long id) {
        log.warn("Delete person by id: {}", id);
        personRepository.deleteById(id);
        return false;
    }

    @Override
    public ResponseEntity<?> savePersonByEmail(Person person) {
        if(personRepository.existsByEmail(person.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already on used");
        }
        savePerson(person);

        ConfirmationToken confirmationToken = new ConfirmationToken(person);
        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(person.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:4200/confirm-account?token="+confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);

        System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());

        return ResponseEntity.ok("Verify email by the link sent on your email address");
    }

    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            Person person = personRepository.findByEmailIgnoreCase(token.getPerson().getEmail());
            person.setEnabled(true);
            personRepository.save(person);
            addRoleToPerson(person.getUsername(), "ROLE_ADMIN");
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(username);
        if (person == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        person.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(person.getUsername(), person.getPassword(), authorities);
    }
}
