package com.app_language.hoctiengtrung_online.Ticket.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "tickets")
public class Ticket {
    @Id
    private String id;

    @Indexed(unique = true)
    private String ticketCode; // Mã vé duy nhất (QR code)

    private String showId;
    private String showName;

    private String ticketTypeId;
    private String ticketTypeName;

    private BigDecimal price;

    // Thông tin người mua
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;

    // Trạng thái: UNPAID, CONFIRMED, USED, CANCELLED
    private String status;

    private LocalDateTime purchaseTime;
    private LocalDateTime createdAt;

    // Nhân viên soát vé (User role)
    private String scannedBy;

    // Cộng tác viên bán vé (để tính lợi tức sau này)
    private String soldBy;

    public Ticket() {
        this.ticketCode = generateTicketCode();
        this.status = "CONFIRMED"; // Mặc định là đã xác nhận cho demo
        this.purchaseTime = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTicketCode() { return ticketCode; }
    public void setTicketCode(String ticketCode) { this.ticketCode = ticketCode; }
    public String getShowId() { return showId; }
    public void setShowId(String showId) { this.showId = showId; }
    public String getShowName() { return showName; }
    public void setShowName(String showName) { this.showName = showName; }
    public String getTicketTypeId() { return ticketTypeId; }
    public void setTicketTypeId(String ticketTypeId) { this.ticketTypeId = ticketTypeId; }
    public String getTicketTypeName() { return ticketTypeName; }
    public void setTicketTypeName(String ticketTypeName) { this.ticketTypeName = ticketTypeName; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
    public String getBuyerEmail() { return buyerEmail; }
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }
    public String getBuyerPhone() { return buyerPhone; }
    public void setBuyerPhone(String buyerPhone) { this.buyerPhone = buyerPhone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getPurchaseTime() { return purchaseTime; }
    public void setPurchaseTime(LocalDateTime purchaseTime) { this.purchaseTime = purchaseTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getScannedBy() { return scannedBy; }
    public void setScannedBy(String scannedBy) { this.scannedBy = scannedBy; }
    public String getSoldBy() { return soldBy; }
    public void setSoldBy(String soldBy) { this.soldBy = soldBy; }

    private String generateTicketCode() {
        // Tạo mã vé ngẫu nhiên đơn giản
        return "TICK" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
}