package com.app_language.hoctiengtrung_online.Ticket.model;

import java.math.BigDecimal;
import java.util.UUID;

// Class này nhúng vào Show, không tạo collection riêng
public class TicketType {

    private String id; // ID riêng cho loại vé để dễ tìm khi mua
    private String name; // Ví dụ: VIP, Thường
    private String description;
    private BigDecimal price;
    private Integer quantity; // Tổng số lượng vé
    private Integer availableQuantity; // Số lượng còn lại
    private Integer soldQuantity; // Số lượng đã bán
    private boolean active;

    public TicketType() {
        // Tự tạo ID khi khởi tạo mới
        this.id = UUID.randomUUID().toString();
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
        // Nếu chưa set available thì mặc định bằng tổng
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