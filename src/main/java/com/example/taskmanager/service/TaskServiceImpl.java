package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponse createTask(TaskRequest request) {

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        Task saved = taskRepository.save(task);

        return mapToResponse(saved);
    }

    @Override
    public TaskResponse getTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        return mapToResponse(task);
    }

    @Override
    public List<TaskResponse> getAllTasks(Pageable pageable, String owner, LocalDate startDate, LocalDate endDate) {

        PageRequest pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
        );
        Page<Task> page;

        Specification<Task> spec = filterTasks(owner, startDate, endDate);
        page = taskRepository.findAll(spec, pageRequest);

        return page.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void updateTask(Long id, TaskRequest request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskRepository.deleteById(id);
    }

    private TaskResponse mapToResponse(Task task) {

        TaskResponse dto = new TaskResponse();

        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setOwner(task.getOwner());
        dto.setCompleted(task.isCompleted());

        return dto;
    }

    private static Specification<Task> filterTasks(String owner, LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (owner != null && !owner.isEmpty()) {
                predicates.add(cb.equal(root.get("owner"), owner));
            }

            if (startDate != null && endDate != null) {
                predicates.add(cb.between(root.get("createdAt"), startDate, endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }
}
