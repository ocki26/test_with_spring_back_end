package com.app_language.hoctiengtrung_online.Ticket.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "companies")
public class Company {
    @Id
    private String id;

    @Indexed(unique = true)
    private String code; // Mã công ty (duy nhất)

    private String name;
    private String description;
    private String address;
    private String phone;
    private String email;

    // Lưu ý: Mật khẩu đăng nhập sẽ nằm ở bảng User (có role COMPANY_USER),
    // nhưng nếu bạn muốn lưu password riêng cho "page công ty" đơn giản thì để đây cũng được.
    // Tuy nhiên chuẩn bảo mật nên dùng User collection để login.

    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Company() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }

    // Getters and Setters
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
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}