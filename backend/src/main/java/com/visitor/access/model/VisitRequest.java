package com.visitor.access.model;

import com.visitor.access.model.enums.ApprovalStatus;
import com.visitor.access.model.enums.RequestStatus;

import java.time.LocalDateTime;

public class VisitRequest {
    private String id;
    private String visitorId;
    private String hostId;
    private String areaId;
    private LocalDateTime expectedEntryTime;
    private LocalDateTime expectedExitTime;
    private String reason;
    private RequestStatus status;
    private boolean requireSecurityApproval;
    private ApprovalStatus securityApprovalStatus;
    private String hostComment;
    private String securityComment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getVisitorId() { return visitorId; }
    public void setVisitorId(String visitorId) { this.visitorId = visitorId; }
    public String getHostId() { return hostId; }
    public void setHostId(String hostId) { this.hostId = hostId; }
    public String getAreaId() { return areaId; }
    public void setAreaId(String areaId) { this.areaId = areaId; }
    public LocalDateTime getExpectedEntryTime() { return expectedEntryTime; }
    public void setExpectedEntryTime(LocalDateTime expectedEntryTime) { this.expectedEntryTime = expectedEntryTime; }
    public LocalDateTime getExpectedExitTime() { return expectedExitTime; }
    public void setExpectedExitTime(LocalDateTime expectedExitTime) { this.expectedExitTime = expectedExitTime; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public boolean isRequireSecurityApproval() { return requireSecurityApproval; }
    public void setRequireSecurityApproval(boolean requireSecurityApproval) { this.requireSecurityApproval = requireSecurityApproval; }
    public ApprovalStatus getSecurityApprovalStatus() { return securityApprovalStatus; }
    public void setSecurityApprovalStatus(ApprovalStatus securityApprovalStatus) { this.securityApprovalStatus = securityApprovalStatus; }
    public String getHostComment() { return hostComment; }
    public void setHostComment(String hostComment) { this.hostComment = hostComment; }
    public String getSecurityComment() { return securityComment; }
    public void setSecurityComment(String securityComment) { this.securityComment = securityComment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
