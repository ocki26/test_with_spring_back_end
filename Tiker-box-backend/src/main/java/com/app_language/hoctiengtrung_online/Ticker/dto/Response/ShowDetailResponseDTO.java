package com.app_language.hoctiengtrung_online.Ticker.dto.Response;



import com.app_language.hoctiengtrung_online.Ticker.model.TicketType;
import java.time.LocalDateTime;
import java.util.List;

public class ShowDetailResponseDTO {
    private String id;
    private String name;
    private String description;
    private String genre;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocationResponseDTO location;
    private List<ArtistResponseDTO> artists;
    private List<String> imageUrls;
    private List<TicketType> ticketTypes;
    private CompanyResponseDTO company;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public LocationResponseDTO getLocation() { return location; }
    public void setLocation(LocationResponseDTO location) { this.location = location; }
    public List<ArtistResponseDTO> getArtists() { return artists; }
    public void setArtists(List<ArtistResponseDTO> artists) { this.artists = artists; }
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    public List<TicketType> getTicketTypes() { return ticketTypes; }
    public void setTicketTypes(List<TicketType> ticketTypes) { this.ticketTypes = ticketTypes; }
    public CompanyResponseDTO getCompany() { return company; }
    public void setCompany(CompanyResponseDTO company) { this.company = company; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}