package com.visitor.access.model;

public class SecurityGuard {
    private String id;
    private String name;
    private String badgeNumber;
    private String phone;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBadgeNumber() { return badgeNumber; }
    public void setBadgeNumber(String badgeNumber) { this.badgeNumber = badgeNumber; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
