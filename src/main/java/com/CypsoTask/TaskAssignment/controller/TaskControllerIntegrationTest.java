package com.CypsoTask.TaskAssignment.controller;

import com.CypsoTask.TaskAssignment.model.Task;
import com.CypsoTask.TaskAssignment.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Assertions;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    void shouldFetchAllTasks() throws Exception {
        Task task = new Task(null, "Test Task", "Description", "Pending");
        taskRepository.save(task);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(task.getTitle())));
    }

    @Test
    void shouldFetchTaskById() throws Exception {
        Task task = new Task(null, "Test Task", "Description", "Pending");
        task = taskRepository.save(task);

        mockMvc.perform(get("/tasks/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(task.getTitle())));
    }

    @Test
    void shouldAddNewTask() throws Exception {
        Task task = new Task(null, "New Task", "New Description", "Pending");

        mockMvc.perform(post("/task/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(task.getTitle())));

        Optional<Task> optionalTask = taskRepository.findById(1L);
        Assertions.assertTrue(optionalTask.isPresent());
        Assertions.assertEquals(task.getTitle(), optionalTask.get().getTitle());
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task task = new Task(null, "Test Task", "Description", "Pending");
        task = taskRepository.save(task);
        Task updatedTask = new Task(task.getId(), "Updated Task", "Updated Description", "Completed");

        mockMvc.perform(put("/update/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(updatedTask.getTitle())));

        Optional<Task> optionalTask = taskRepository.findById(task.getId());
        Assertions.assertTrue(optionalTask.isPresent());
        Assertions.assertEquals(updatedTask.getTitle(), optionalTask.get().getTitle());
    }

    @Test
    void shouldDeleteTask() throws Exception {
        Task task = new Task(null, "Test Task", "Description", "Pending");
        task = taskRepository.save(task);

        mockMvc.perform(delete("/delete/{id}", task.getId()))
                .andExpect(status().isNoContent());

        Optional<Task> deletedTask = taskRepository.findById(task.getId());
        Assertions.assertFalse(deletedTask.isPresent());
    }
}
