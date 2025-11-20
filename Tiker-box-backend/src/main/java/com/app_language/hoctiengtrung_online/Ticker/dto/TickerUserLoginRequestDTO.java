package com.app_language.hoctiengtrung_online.Ticker.dto;



import jakarta.validation.constraints.NotBlank;

public class TickerUserLoginRequestDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    // Constructors
    public TickerUserLoginRequestDTO() {}

    public TickerUserLoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserLoginRequestDTO{" +
                "username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}