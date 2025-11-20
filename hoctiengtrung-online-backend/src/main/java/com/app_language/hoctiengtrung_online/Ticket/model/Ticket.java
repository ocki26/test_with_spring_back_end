package com.app_language.hoctiengtrung_online.Ticket.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "tickets")
public class Ticket {
    @Id
    private String id;

    @Indexed(unique = true)
    private String ticketCode;

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
    private String scannedBy;
    private String soldBy;

    // Constructor sẽ không tự generate ticketCode nữa
    public Ticket() {
        this.status = "CONFIRMED";
        this.purchaseTime = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }
}