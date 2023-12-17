package com.demo.organizing;

import com.demo.organizing.entities.Person;
import com.demo.organizing.entities.Role;
import com.demo.organizing.entities.Task;
import com.demo.organizing.enums.TaskStatus;
import com.demo.organizing.enums.TaskType;
import com.demo.organizing.service.PersonService;
import com.demo.organizing.service.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class OrganizingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrganizingApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(PersonService personService, TaskService taskService) throws ParseException {
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = DateFor.parse("08/07/2019");
        Date date2 = DateFor.parse("06/06/2023");
        Date date3 = DateFor.parse("19/06/2023");
        return args -> {
            personService.saveRole(new Role(null, "ROLE_USER"));
            personService.saveRole(new Role(null, "ROLE_MANAGER"));
            personService.saveRole(new Role(null, "ROLE_ADMIN"));
            personService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));
            List<Task> taskList = new ArrayList<>();
            Person person1 = personService.savePerson(new Person(null, "admin@gmail.com", "admin", "1",
                    new ArrayList<>(), new ArrayList<>(),new ArrayList<>(), 5L, true));
            personService.savePerson(new Person(null, "user@gmail.com", "user", "1",
                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),5L, true));
            personService.savePerson(new Person(null, "manager@gmail.com", "manager", "1",
                    new ArrayList<>(), new ArrayList<>(),new ArrayList<>(),5L, true));
            Person person = personService.savePerson(new Person(null, "superAdmin@gmail.com", "superAdmin", "1",
                    new ArrayList<>(), taskList,new ArrayList<>(), 5L, true));
            taskService.saveTask(new Task(1L, "Complete English exam, repeating the structure of the summary", "Prepare for exam",
                    TaskStatus.STARTED, TaskType.DAILY, date1, true, person), person.getUsername());
            taskService.saveTask(new Task(2L, "Finish integrate chat with AI", "AI API",
                    TaskStatus.PLANNED, TaskType.WEEKLY, date2, true, person), person.getUsername());
            taskService.saveTask(new Task(2L, "Buy a new season ticket to the gym", "Sport title",
                    TaskStatus.PLANNED, TaskType.WEEKLY, date2, false, person), person.getUsername());
            taskService.saveTask(new Task(2L, "Clean up the utility room, fix the faucet (leaking water)", "Home",
                    TaskStatus.PLANNED, TaskType.WEEKLY, date2, true, person), person.getUsername());
            taskService.saveTask(new Task(2L, "Complete learning Jaspersoft and start learning java17", "Improve tech-skill",
                    TaskStatus.PLANNED, TaskType.MONTHLY, date2, true, person), person.getUsername());
            taskService.saveTask(new Task(3L, "48 laws of power, The subconscious can do anything, Night in Lisbon", "Books",
                    TaskStatus.STARTED, TaskType.MONTHLY, date3, true, person1), person1.getUsername());

            personService.addRoleToPerson("user", "ROLE_USER");
            personService.addRoleToPerson("user", "ROLE_MANAGER");
            personService.addRoleToPerson("manager", "ROLE_MANAGER");
            personService.addRoleToPerson("admin", "ROLE_ADMIN");
            personService.addRoleToPerson("superAdmin", "ROLE_ADMIN");
            personService.addRoleToPerson("superAdmin", "ROLE_SUPER_ADMIN");
            personService.addRoleToPerson("superAdmin", "ROLE_USER");
        };
    }
}
