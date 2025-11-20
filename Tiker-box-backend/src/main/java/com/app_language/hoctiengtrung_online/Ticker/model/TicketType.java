package com.app_language.hoctiengtrung_online.Ticker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
@Document(collection = "users")
public class TicketType {
    @Id
    private String id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Integer availableQuantity;
    private Integer soldQuantity;
    private boolean active;

    public TicketType() {
        this.soldQuantity = 0;
        this.active = true;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        if (this.availableQuantity == null) {
            this.availableQuantity = quantity;
        }
    }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
    public Integer getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(Integer soldQuantity) { this.soldQuantity = soldQuantity; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}