package com.Java_Project.Task_Manager.service;

import com.Java_Project.Task_Manager.entity.Task;
import com.Java_Project.Task_Manager.repository.TaskRepository;
import com.Java_Project.Task_Manager.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(String username, Task task)
    {
        // Verify user exists before creating task
        userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setUsername(username);
        task.setStatus("Pending");

        return taskRepository.save(task);
    }

    public List<Task> getUserTasks(String username)
    {
        return taskRepository.findByUsername(username);
    }
}
