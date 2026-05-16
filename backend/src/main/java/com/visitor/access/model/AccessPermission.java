package com.visitor.access.model;

import com.visitor.access.model.enums.PermissionLevel;
import com.visitor.access.model.enums.PermissionStatus;

import java.time.LocalDateTime;
import java.util.List;

public class AccessPermission {
    private String id;
    private String requestId;
    private String visitorId;
    private String permissionCode;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private List<String> accessibleAreas;
    private PermissionLevel permissionLevel;
    private PermissionStatus status;
    private LocalDateTime actualEntryTime;
    private LocalDateTime actualExitTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public String getVisitorId() { return visitorId; }
    public void setVisitorId(String visitorId) { this.visitorId = visitorId; }
    public String getPermissionCode() { return permissionCode; }
    public void setPermissionCode(String permissionCode) { this.permissionCode = permissionCode; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    public LocalDateTime getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }
    public List<String> getAccessibleAreas() { return accessibleAreas; }
    public void setAccessibleAreas(List<String> accessibleAreas) { this.accessibleAreas = accessibleAreas; }
    public PermissionLevel getPermissionLevel() { return permissionLevel; }
    public void setPermissionLevel(PermissionLevel permissionLevel) { this.permissionLevel = permissionLevel; }
    public PermissionStatus getStatus() { return status; }
    public void setStatus(PermissionStatus status) { this.status = status; }
    public LocalDateTime getActualEntryTime() { return actualEntryTime; }
    public void setActualEntryTime(LocalDateTime actualEntryTime) { this.actualEntryTime = actualEntryTime; }
    public LocalDateTime getActualExitTime() { return actualExitTime; }
    public void setActualExitTime(LocalDateTime actualExitTime) { this.actualExitTime = actualExitTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
