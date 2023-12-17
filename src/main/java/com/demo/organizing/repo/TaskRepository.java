package com.demo.organizing.repo;

import com.demo.organizing.entities.Person;
import com.demo.organizing.entities.Task;
import com.demo.organizing.enums.TaskStatus;
import com.demo.organizing.enums.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByTitle(String title);
    Task findByStatus(TaskStatus status);
    Task findByType(TaskType type);
    List<Task> findAllByPerson(Person person);

}