package com.visitor.access.dto.response;

import com.visitor.access.model.VisitRequest;
import com.visitor.access.model.enums.ApprovalStatus;
import com.visitor.access.model.enums.RequestStatus;

import java.time.LocalDateTime;

public class VisitRequestDetailDTO {
    private String id;
    private String visitorId;
    private String visitorName;
    private String hostId;
    private String hostName;
    private String areaId;
    private String areaName;
    private String areaLevel;
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

    public static VisitRequestDetailDTO fromVisitRequest(VisitRequest request, String visitorName, String hostName, String areaName, String areaLevel) {
        VisitRequestDetailDTO dto = new VisitRequestDetailDTO();
        dto.setId(request.getId());
        dto.setVisitorId(request.getVisitorId());
        dto.setVisitorName(visitorName);
        dto.setHostId(request.getHostId());
        dto.setHostName(hostName);
        dto.setAreaId(request.getAreaId());
        dto.setAreaName(areaName);
        dto.setAreaLevel(areaLevel);
        dto.setExpectedEntryTime(request.getExpectedEntryTime());
        dto.setExpectedExitTime(request.getExpectedExitTime());
        dto.setReason(request.getReason());
        dto.setStatus(request.getStatus());
        dto.setRequireSecurityApproval(request.isRequireSecurityApproval());
        dto.setSecurityApprovalStatus(request.getSecurityApprovalStatus());
        dto.setHostComment(request.getHostComment());
        dto.setSecurityComment(request.getSecurityComment());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getVisitorId() { return visitorId; }
    public void setVisitorId(String visitorId) { this.visitorId = visitorId; }
    public String getVisitorName() { return visitorName; }
    public void setVisitorName(String visitorName) { this.visitorName = visitorName; }
    public String getHostId() { return hostId; }
    public void setHostId(String hostId) { this.hostId = hostId; }
    public String getHostName() { return hostName; }
    public void setHostName(String hostName) { this.hostName = hostName; }
    public String getAreaId() { return areaId; }
    public void setAreaId(String areaId) { this.areaId = areaId; }
    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }
    public String getAreaLevel() { return areaLevel; }
    public void setAreaLevel(String areaLevel) { this.areaLevel = areaLevel; }
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
