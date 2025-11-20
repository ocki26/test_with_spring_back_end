package com.app_language.hoctiengtrung_online.Ticker.dto;



import com.app_language.hoctiengtrung_online.Ticker.model.TicketType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class ShowUpdateRequestDTO {
    @NotBlank(message = "Show name is required")
    private String name;

    private String description;
    private String genre;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @NotBlank(message = "Location ID is required")
    private String locationId;

    private String locationAddress;
    private List<String> artistIds;
    private List<String> imageUrls;
    private List<TicketType> ticketTypes;

    // Getters and Setters
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
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    public List<TicketType> getTicketTypes() { return ticketTypes; }
    public void setTicketTypes(List<TicketType> ticketTypes) { this.ticketTypes = ticketTypes; }
}