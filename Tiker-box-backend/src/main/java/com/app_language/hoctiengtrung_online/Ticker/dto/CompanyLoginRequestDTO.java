package com.app_language.hoctiengtrung_online.Ticker.dto;



import jakarta.validation.constraints.NotBlank;

public class CompanyLoginRequestDTO {
    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Password is required")
    private String password;

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}