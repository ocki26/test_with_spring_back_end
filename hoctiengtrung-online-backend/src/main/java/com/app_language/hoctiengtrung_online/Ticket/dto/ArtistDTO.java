package com.app_language.hoctiengtrung_online.Ticket.dto;

import jakarta.validation.constraints.NotBlank;

public class ArtistDTO {
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Company code is required")
    private String companyCode;

    private String imageUrl;

    // Constructor mặc định (QUAN TRỌNG)
    public ArtistDTO() {}

    // Constructor với tham số
    public ArtistDTO(String name, String description, String companyCode, String imageUrl) {
        this.name = name;
        this.description = description;
        this.companyCode = companyCode;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public String toString() {
        return "ArtistDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}