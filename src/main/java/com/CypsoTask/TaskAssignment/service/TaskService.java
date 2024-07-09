package com.CypsoTask.TaskAssignment.service;

import com.CypsoTask.TaskAssignment.exception.TaskAlreadyExistsException;
import com.CypsoTask.TaskAssignment.exception.TaskNotFoundException;
import com.CypsoTask.TaskAssignment.model.Task;
import com.CypsoTask.TaskAssignment.repository.TaskRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task addTask(Task task) {
        if (taskAlreadyExists(task.getStatus())) {
            throw new TaskAlreadyExistsException(task.getStatus() + " already exists!");
        }
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task, Long id) {
        return taskRepository.findById(id).map(existingTask -> {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setStatus(task.getStatus());
            return taskRepository.save(existingTask);
        }).orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }
        taskRepository.deleteById(id);
    }

    private boolean taskAlreadyExists(String status) {
        return taskRepository.findByStatus(status).isPresent();
    }
}