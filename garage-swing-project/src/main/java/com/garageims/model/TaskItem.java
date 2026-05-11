package com.garageims.model;

import java.time.LocalDate;

public class TaskItem {
    private int id;
    private Integer assignedTo;
    private String assignedToName;
    private String title;
    private String description;
    private String status;
    private LocalDate taskDate;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Integer assignedTo) { this.assignedTo = assignedTo; }
    public String getAssignedToName() { return assignedToName; }
    public void setAssignedToName(String assignedToName) { this.assignedToName = assignedToName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getTaskDate() { return taskDate; }
    public void setTaskDate(LocalDate taskDate) { this.taskDate = taskDate; }
}
