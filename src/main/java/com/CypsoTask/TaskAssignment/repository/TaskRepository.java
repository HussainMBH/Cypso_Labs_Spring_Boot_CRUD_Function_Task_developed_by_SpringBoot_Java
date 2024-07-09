package com.CypsoTask.TaskAssignment.repository;
import com.CypsoTask.TaskAssignment.model.Task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByStatus(String status);
}
