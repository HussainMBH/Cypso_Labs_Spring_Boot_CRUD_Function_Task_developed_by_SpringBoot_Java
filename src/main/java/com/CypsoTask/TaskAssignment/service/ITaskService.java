package com.CypsoTask.TaskAssignment.service;
import com.CypsoTask.TaskAssignment.model.Task;

import java.util.List;

public interface ITaskService {
    List<Task> getTasks();
    Task addTask(Task task);
    Task updateTask(Task task, Long id);
    Task getTaskById(Long id);
    void deleteTask(Long id);
}
