package com.app_language.hoctiengtrung_online.Ticket.dto;



import java.math.BigDecimal;

public class TicketTypeDTO {
    private String id; // Có thể null khi tạo mới
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;

    // Getters Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}