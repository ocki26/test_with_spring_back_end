// Show.java - THÊM inner class cho ảnh
package com.app_language.hoctiengtrung_online.Ticket.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "shows")
public class Show {
    @Id
    private String id;

    @Version
    private Long version;

    private String name;
    private String description;
    private String genre;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String locationId;
    private String locationAddress;
    private List<String> artistIds;

    // THAY ĐỔI: Thay List<String> imageUrls bằng List<ShowImage>
    private List<ShowImage> images;

    private List<TicketType> ticketTypes;
    private String companyId;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Show() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }

    // THÊM: Inner class để lưu thông tin ảnh
    public static class ShowImage {
        private byte[] imageData;
        private String imageContentType;
        private String imageFileName;
        private Integer displayOrder; // Thứ tự hiển thị

        public ShowImage() {}

        public ShowImage(byte[] imageData, String imageContentType, String imageFileName, Integer displayOrder) {
            this.imageData = imageData;
            this.imageContentType = imageContentType;
            this.imageFileName = imageFileName;
            this.displayOrder = displayOrder;
        }

        // Getters and Setters
        public byte[] getImageData() { return imageData; }
        public void setImageData(byte[] imageData) { this.imageData = imageData; }
        public String getImageContentType() { return imageContentType; }
        public void setImageContentType(String imageContentType) { this.imageContentType = imageContentType; }
        public String getImageFileName() { return imageFileName; }
        public void setImageFileName(String imageFileName) { this.imageFileName = imageFileName; }
        public Integer getDisplayOrder() { return displayOrder; }
        public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }
    public String getLocationAddress() { return locationAddress; }
    public void setLocationAddress(String locationAddress) { this.locationAddress = locationAddress; }
    public List<String> getArtistIds() { return artistIds; }
    public void setArtistIds(List<String> artistIds) { this.artistIds = artistIds; }

    // THAY ĐỔI: Getter/Setter cho images
    public List<ShowImage> getImages() { return images; }
    public void setImages(List<ShowImage> images) { this.images = images; }

    public List<TicketType> getTicketTypes() { return ticketTypes; }
    public void setTicketTypes(List<TicketType> ticketTypes) { this.ticketTypes = ticketTypes; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}