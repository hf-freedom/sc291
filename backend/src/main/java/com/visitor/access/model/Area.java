package com.visitor.access.model;

import com.visitor.access.model.enums.PermissionLevel;

import java.time.LocalDateTime;

public class Area {
    private String id;
    private String name;
    private String description;
    private PermissionLevel requiredPermissionLevel;
    private String status;
    private LocalDateTime createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public PermissionLevel getRequiredPermissionLevel() { return requiredPermissionLevel; }
    public void setRequiredPermissionLevel(PermissionLevel requiredPermissionLevel) { this.requiredPermissionLevel = requiredPermissionLevel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
