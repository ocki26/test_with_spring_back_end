package com.app_language.hoctiengtrung_online.Ticker.dto;



import jakarta.validation.constraints.NotBlank;

public class ArtistRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Company code is required")
    private String companyCode;

    private String imageUrl;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}