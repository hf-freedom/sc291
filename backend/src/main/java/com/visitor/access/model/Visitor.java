package com.visitor.access.model;

import com.visitor.access.model.enums.RiskLevel;

import java.time.LocalDateTime;

public class Visitor {
    private String id;
    private String name;
    private String phone;
    private String idCard;
    private String email;
    private RiskLevel riskLevel;
    private LocalDateTime blacklistUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    public LocalDateTime getBlacklistUntil() { return blacklistUntil; }
    public void setBlacklistUntil(LocalDateTime blacklistUntil) { this.blacklistUntil = blacklistUntil; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
