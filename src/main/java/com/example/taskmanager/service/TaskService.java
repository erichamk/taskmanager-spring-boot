package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);

    TaskResponse getTask(Long id);

    List<TaskResponse> getAllTasks(Pageable pageable, String owner, LocalDate startDate, LocalDate endDate);

    void updateTask(Long id, TaskRequest request);

    void deleteTask(Long id);

}
