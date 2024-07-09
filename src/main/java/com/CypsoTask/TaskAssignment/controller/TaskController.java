package com.CypsoTask.TaskAssignment.controller;

import com.CypsoTask.TaskAssignment.model.Task;
import com.CypsoTask.TaskAssignment.service.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // Allowing client application to consume the backend
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final ITaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks() {
        List<Task> tasks = taskService.getTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/task/add")
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        Task addedTask = taskService.addTask(task);
        return new ResponseEntity<>(addedTask, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(@RequestBody Task task, @PathVariable Long id) {
        Task updatedTask = taskService.updateTask(task, id);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}
