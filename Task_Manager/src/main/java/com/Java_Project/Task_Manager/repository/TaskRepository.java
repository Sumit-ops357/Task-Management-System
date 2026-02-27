package com.Java_Project.Task_Manager.repository;

import com.Java_Project.Task_Manager.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {

    List<Task> findByUsername(String username);
}