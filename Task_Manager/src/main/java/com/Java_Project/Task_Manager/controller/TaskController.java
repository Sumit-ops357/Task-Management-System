package com.Java_Project.Task_Manager.controller;

import com.Java_Project.Task_Manager.entity.Task;
import com.Java_Project.Task_Manager.service.TaskService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, Authentication authentication)
    {
        String username = authentication.getName();
        return taskService.createTask(username, task);
    }

    @GetMapping
    public List<Task> getTasks(Authentication authentication)
    {
        String username = authentication.getName();
        return taskService.getUserTasks(username);
    }
}
