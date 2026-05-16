package com.visitor.access.dto.response;

public class CheckInResponse {
    private String permissionId;
    private String actualEntryTime;
    private String status;
    private boolean isLate;
    private String message;

    public String getPermissionId() { return permissionId; }
    public void setPermissionId(String permissionId) { this.permissionId = permissionId; }
    public String getActualEntryTime() { return actualEntryTime; }
    public void setActualEntryTime(String actualEntryTime) { this.actualEntryTime = actualEntryTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isLate() { return isLate; }
    public void setLate(boolean late) { isLate = late; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
