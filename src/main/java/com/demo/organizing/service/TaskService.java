package com.demo.organizing.service;

import com.demo.organizing.entities.Task;
import com.demo.organizing.enums.TaskStatus;
import com.demo.organizing.enums.TaskType;

import java.util.List;

public interface TaskService {
    Task saveTask(Task task, String username);
    boolean updateTask(Task taskOrig, long id, String username);
    Task getTaskByTitle(String title);
    Task getTaskByStatus(TaskStatus status);
    int getAmountOfTaskByStatus(TaskStatus status);
    List<Task> getTaskByType(TaskType type, String username);
    List<Task> getTasks();
    boolean deleteTaskById(Long id);
}
