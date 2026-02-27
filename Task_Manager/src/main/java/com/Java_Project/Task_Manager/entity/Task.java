package com.Java_Project.Task_Manager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    private String title;
    private String description;
    private String status;   // Pending, Completed

    private String username; // owner's username (replaces JPA @ManyToOne User)

    // Constructors
    public Task() {}

    public Task(String id, String title, String description, String status, String username) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.username = username;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // Builder
    public static TaskBuilder builder() { return new TaskBuilder(); }

    public static class TaskBuilder {
        private String id;
        private String title;
        private String description;
        private String status;
        private String username;

        public TaskBuilder id(String id) { this.id = id; return this; }
        public TaskBuilder title(String title) { this.title = title; return this; }
        public TaskBuilder description(String description) { this.description = description; return this; }
        public TaskBuilder status(String status) { this.status = status; return this; }
        public TaskBuilder username(String username) { this.username = username; return this; }

        public Task build() {
            return new Task(id, title, description, status, username);
        }
    }
}
