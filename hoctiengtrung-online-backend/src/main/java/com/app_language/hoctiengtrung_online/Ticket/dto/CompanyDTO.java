package com.app_language.hoctiengtrung_online.Ticket.dto;
import lombok.Data; // Nếu bạn dùng lombok, nếu không thì phải viết getter/setter thủ công

// Tôi sẽ viết thủ công Getter/Setter cho chắc chắn bạn chạy được
public class CompanyDTO {
    private String id;
    private String code;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String email;
    private String password; // Dùng khi tạo công ty để login page công ty

    // Getters Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}