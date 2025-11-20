package com.app_language.hoctiengtrung_online.Ticket.dto;



import java.time.LocalDateTime;
import java.util.List;

public class ShowDTO {
    private String id;
    private String name;
    private String description;
    private String genre;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String locationId; // Chọn location có sẵn
    private List<String> artistIds; // Chọn nghệ sĩ
    @jakarta.validation.Valid
    private List<TicketTypeDTO> ticketTypes; // Nhập loại vé (giá, số lượng)
    private String companyId; // Nếu admin tạo giùm công ty

    // Getters Setters
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
    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }
    public List<String> getArtistIds() { return artistIds; }
    public void setArtistIds(List<String> artistIds) { this.artistIds = artistIds; }
    public List<TicketTypeDTO> getTicketTypes() { return ticketTypes; }
    public void setTicketTypes(List<TicketTypeDTO> ticketTypes) { this.ticketTypes = ticketTypes; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
}