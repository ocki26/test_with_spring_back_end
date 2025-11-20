package com.app_language.hoctiengtrung_online.Ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Artist data transfer object")
public class ArtistDTO {
    private String id;

    @NotBlank(message = "Name is required")
    @Schema(description = "Artist name", example = "Sơn Tùng M-TP", required = true)
    private String name;

    @Schema(description = "Artist description", example = "Ca sĩ nhạc Pop nổi tiếng Việt Nam")
    private String description;

    @NotBlank(message = "Company code is required")
    @Schema(description = "Company code that the artist belongs to", example = "COMP001", required = true)
    private String companyCode;

    @Schema(description = "Artist image URL", example = "https://example.com/images/artist.jpg")
    private String imageUrl;

    // Constructors, Getters and Setters giữ nguyên
    public ArtistDTO() {
    }

    public ArtistDTO(String name, String description, String companyCode) {
        this.name = name;
        this.description = description;
        this.companyCode = companyCode;
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
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", companyCode='" + companyCode + '\'' +
                '}';
    }
}