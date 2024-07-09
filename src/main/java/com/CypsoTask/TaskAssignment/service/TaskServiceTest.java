package com.CypsoTask.TaskAssignment.service;

import com.CypsoTask.TaskAssignment.exception.TaskAlreadyExistsException;
import com.CypsoTask.TaskAssignment.exception.TaskNotFoundException;
import com.CypsoTask.TaskAssignment.model.Task;
import com.CypsoTask.TaskAssignment.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // line initializes the mocks
    }

    @Test
    void testGetTasks() {
        Task task1 = new Task(1L, "Task 1", "Morning Study", "Ongoing");
        Task task2 = new Task(2L, "Task 2", "Leetcode Coding", "Assigned");
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getTasks();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testAddTask() {
        Task task = new Task(1L, "Task 1", "Morning Study", "Ongoing");
        when(taskRepository.findByStatus(task.getStatus())).thenReturn(Optional.empty());
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.addTask(task);

        assertNotNull(createdTask);
        assertEquals("Task 1", createdTask.getTitle());
        verify(taskRepository, times(1)).findByStatus(task.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testAddTask_TaskAlreadyExists() {
        Task task = new Task(1L, "Task 1", "Morning Study", "Ongoing");
        when(taskRepository.findByStatus(task.getStatus())).thenReturn(Optional.of(task));

        assertThrows(TaskAlreadyExistsException.class, () -> taskService.addTask(task));
        verify(taskRepository, times(1)).findByStatus(task.getStatus());
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void testUpdateTask() {
        Task existingTask = new Task(1L, "Task 1", "Morning Study", "Ongoing");
        Task updatedTask = new Task(1L, "Updated Task", "Updated Studies Matter", "DONE");
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(updatedTask, 1L);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("DONE", result.getStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void testUpdateTask_TaskNotFound() {
        Task task = new Task(1L, "Task 1", "Morning Study", "Ongoing");
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(task, 1L));
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void testGetTaskById() {
        Task task = new Task(1L, "Task 1", "Morning Study", "Ongoing");
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Task 1", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskById_TaskNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(taskRepository).deleteById(anyLong());

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTask_TaskNotFound() {
        when(taskRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(0)).deleteById(anyLong());
    }
}
