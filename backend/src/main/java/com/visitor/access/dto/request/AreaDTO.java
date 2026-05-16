package com.visitor.access.dto.request;

import com.visitor.access.model.enums.PermissionLevel;

public class AreaDTO {
    private String name;
    private String description;
    private PermissionLevel requiredPermissionLevel;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public PermissionLevel getRequiredPermissionLevel() { return requiredPermissionLevel; }
    public void setRequiredPermissionLevel(PermissionLevel requiredPermissionLevel) { this.requiredPermissionLevel = requiredPermissionLevel; }
}
