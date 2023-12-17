package com.demo.organizing.api;

import com.demo.organizing.entities.Task;
import com.demo.organizing.enums.TaskStatus;
import com.demo.organizing.enums.TaskType;
import com.demo.organizing.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks() {
        return ResponseEntity.ok().body(taskService.getTasks());
    }

    @GetMapping("/task/{title}")
    public ResponseEntity<Task> getTaskByTitleTest(@PathVariable(name = "title") String title) {
        return ResponseEntity.ok().body(taskService.getTaskByTitle(title));
    }

    @GetMapping("/task/status/{status}")
    public ResponseEntity<Task> getTaskByStatus(@PathVariable(name = "status") TaskStatus status) {
        return ResponseEntity.ok().body(taskService.getTaskByStatus(status));
    }
    @GetMapping("/task/{status}/count")
    public ResponseEntity<Integer> getAmountOfTaskByStatus(@PathVariable(name = "status") TaskStatus status) {
        return ResponseEntity.ok().body(taskService.getAmountOfTaskByStatus(status));
    }

    @GetMapping("/task/type/{type}")
    public ResponseEntity<List<Task>> getTaskByType(@PathVariable(name = "type") TaskType type, @RequestParam(name = "username") String username){
        return ResponseEntity.ok().body(taskService.getTaskByType(type, username));
    }

    @PostMapping("/task/save")
    public ResponseEntity<Task> saveTask(@RequestBody Task task, @RequestParam(name = "username") String username) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/person/save").toUriString());
        return ResponseEntity.created(uri).body(taskService.saveTask(task, username));
    }

    @PutMapping("/task/update")
    public ResponseEntity<Task> updateTask(@RequestBody Task task, @RequestParam(name = "username") String username) {
        final boolean updated = taskService.updateTask(task, task.getId(), username);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<?> deleteTaskById(@PathVariable(name = "id") Long id) {
        final boolean deleted = taskService.deleteTaskById(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

}
