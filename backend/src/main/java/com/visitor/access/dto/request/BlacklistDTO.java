package com.visitor.access.dto.request;

public class BlacklistDTO {
    private String visitorId;
    private String reason;
    private String expireAt;

    public String getVisitorId() { return visitorId; }
    public void setVisitorId(String visitorId) { this.visitorId = visitorId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getExpireAt() { return expireAt; }
    public void setExpireAt(String expireAt) { this.expireAt = expireAt; }
}
