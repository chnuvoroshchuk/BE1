package com.demo.organizing.service.impl;

import com.demo.organizing.entities.Person;
import com.demo.organizing.entities.Task;
import com.demo.organizing.enums.TaskStatus;
import com.demo.organizing.enums.TaskType;
import com.demo.organizing.repo.PersonRepository;
import com.demo.organizing.repo.TaskRepository;
import com.demo.organizing.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final PersonRepository personRepository;

    @Override
    public Task saveTask(Task task, String username) {
        log.info("Saving new task with title {} to the database", task.getTitle());
        Person person = personRepository.findByUsername(username);
        task.setPerson(person);
        return taskRepository.save(task);
    }

    @Override
    public boolean updateTask(Task taskOrig, long id, String username) {
        Person person = personRepository.findByUsername(username);
        if (taskRepository.existsById(id)) {
            Optional<Task> found = taskRepository.findById(id);
            if (found.isPresent()) {
                Task task = found.get();
                task.setId(id);
                task.setPerson(person);
                task.setType(taskOrig.getType());
                task.setTitle(taskOrig.getTitle());
                task.setStatus(taskOrig.getStatus());
                task.setDescription(taskOrig.getDescription());
                task.setDuration(taskOrig.getDuration());
                taskRepository.save(task);
                return true;
            }
        }
        return false;
    }

    @Override
    public Task getTaskByTitle(String title) {
        log.info("Fetching task with title {} to the database", title);
        return taskRepository.findByTitle(title);
    }

    @Override
    public Task getTaskByStatus(TaskStatus status) {

        log.info("Fetch task by status: {}", status);
        return taskRepository.findByStatus(status);
    }
    @Override
    public int getAmountOfTaskByStatus(TaskStatus status){
        List<Task> tasks = taskRepository.findAll();
        return (int) tasks.stream().filter(task -> task.getStatus().equals(status)).count();
    }

    @Override
    public List<Task> getTaskByType(TaskType type, String username) {
        Person person = personRepository.findByUsername(username);
        List<Task> tasks = taskRepository.findAllByPerson(person);
        log.info("Fetch task by type: {}", type);
        return tasks.stream().filter(task -> task.getType().equals(type)).collect(Collectors.toList());
    }

    @Override
    public List<Task> getTasks() {
        log.info("Fetching all tasks");
        return taskRepository.findAll();
    }

    @Override
    public boolean deleteTaskById(Long id) {
        log.warn("Delete task by id: {}", id);
        taskRepository.deleteById(id);
        return true;
    }
}
