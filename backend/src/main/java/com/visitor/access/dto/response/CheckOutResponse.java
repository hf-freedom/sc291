package com.visitor.access.dto.response;

public class CheckOutResponse {
    private String permissionId;
    private String actualExitTime;
    private String status;
    private boolean isOverstay;
    private String message;

    public String getPermissionId() { return permissionId; }
    public void setPermissionId(String permissionId) { this.permissionId = permissionId; }
    public String getActualExitTime() { return actualExitTime; }
    public void setActualExitTime(String actualExitTime) { this.actualExitTime = actualExitTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isOverstay() { return isOverstay; }
    public void setOverstay(boolean overstay) { isOverstay = overstay; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
