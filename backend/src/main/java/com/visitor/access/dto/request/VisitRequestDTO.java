package com.visitor.access.dto.request;

public class VisitRequestDTO {
    private String visitorId;
    private String hostId;
    private String areaId;
    private String expectedEntryTime;
    private String expectedExitTime;
    private String reason;

    public String getVisitorId() { return visitorId; }
    public void setVisitorId(String visitorId) { this.visitorId = visitorId; }
    public String getHostId() { return hostId; }
    public void setHostId(String hostId) { this.hostId = hostId; }
    public String getAreaId() { return areaId; }
    public void setAreaId(String areaId) { this.areaId = areaId; }
    public String getExpectedEntryTime() { return expectedEntryTime; }
    public void setExpectedEntryTime(String expectedEntryTime) { this.expectedEntryTime = expectedEntryTime; }
    public String getExpectedExitTime() { return expectedExitTime; }
    public void setExpectedExitTime(String expectedExitTime) { this.expectedExitTime = expectedExitTime; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
