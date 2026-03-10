package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponse create(@RequestBody @Valid TaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @GetMapping
    public List<TaskResponse> getAll() {
        return taskService.getAllTasks();
    }

    @PutMapping("/{id}")
    public TaskResponse update(
            @PathVariable Long id,
            @RequestBody TaskRequest request) {

        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
