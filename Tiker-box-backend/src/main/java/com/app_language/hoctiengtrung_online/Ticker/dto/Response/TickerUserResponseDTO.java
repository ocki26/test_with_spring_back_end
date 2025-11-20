package com.app_language.hoctiengtrung_online.Ticker.dto.Response;




import java.time.LocalDateTime;

public class TickerUserResponseDTO {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String role;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor mặc định - QUAN TRỌNG
    public TickerUserResponseDTO() {
    }

    // Constructor với tham số
    public TickerUserResponseDTO(String id, String username, String email, String fullName,
                                 String phone, String role, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.active = active;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "TickerUserResponseDTO{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}