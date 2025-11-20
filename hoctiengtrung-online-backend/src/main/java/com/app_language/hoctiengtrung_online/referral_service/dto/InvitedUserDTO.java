package com.app_language.hoctiengtrung_online.referral_service.dto;

public class InvitedUserDTO {
    private String id;
    private String username;
    private String email;

    public InvitedUserDTO(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getter + Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

