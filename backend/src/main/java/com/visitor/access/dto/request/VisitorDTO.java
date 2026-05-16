package com.visitor.access.dto.request;

public class VisitorDTO {
    private String name;
    private String phone;
    private String idCard;
    private String email;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
