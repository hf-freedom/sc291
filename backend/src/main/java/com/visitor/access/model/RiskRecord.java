package com.visitor.access.model;

import com.visitor.access.model.enums.RiskType;

import java.time.LocalDateTime;

public class RiskRecord {
    private String id;
    private String visitorId;
    private String requestId;
    private RiskType riskType;
    private String severity;
    private String description;
    private LocalDateTime recordedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getVisitorId() { return visitorId; }
    public void setVisitorId(String visitorId) { this.visitorId = visitorId; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public RiskType getRiskType() { return riskType; }
    public void setRiskType(RiskType riskType) { this.riskType = riskType; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}
