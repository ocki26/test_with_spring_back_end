// ArtistDTO.java - CÓ THỂ SỬA để không trả về binary data
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

    // XÓA: private String imageUrl;
    // THÊM: field để frontend biết có ảnh hay không
    @Schema(description = "Whether artist has image")
    private Boolean hasImage = false;

    // Constructors
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

    // XÓA: getter/setter imageUrl
    // THÊM: getter/setter hasImage
    public Boolean getHasImage() { return hasImage; }
    public void setHasImage(Boolean hasImage) { this.hasImage = hasImage; }

    @Override
    public String toString() {
        return "ArtistDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", hasImage=" + hasImage +
                '}';
    }
}