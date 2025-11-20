package com.app_language.hoctiengtrung_online.Ticket.dto;




import com.app_language.hoctiengtrung_online.Ticket.model.TicketType;

import java.time.LocalDateTime;
import java.util.List;

public class ShowRequestDTO {
    private String name;
    private String description;
    private String genre;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String locationId;
    private List<String> artistIds;
    private List<TicketType> ticketTypes;
    private String companyId;

    // Constructors
    public ShowRequestDTO() {}

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
    public List<String> getArtistIds() { return artistIds; }
    public void setArtistIds(List<String> artistIds) { this.artistIds = artistIds; }
    public List<TicketType> getTicketTypes() { return ticketTypes; }
    public void setTicketTypes(List<TicketType> ticketTypes) { this.ticketTypes = ticketTypes; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
}