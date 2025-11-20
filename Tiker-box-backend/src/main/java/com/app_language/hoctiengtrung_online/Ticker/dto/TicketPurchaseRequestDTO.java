package com.app_language.hoctiengtrung_online.Ticker.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TicketPurchaseRequestDTO {
    @NotBlank(message = "Show ID is required")
    private String showId;

    @NotBlank(message = "Ticket type ID is required")
    private String ticketTypeId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotBlank(message = "Buyer name is required")
    private String buyerName;

    @NotBlank(message = "Buyer email is required")
    private String buyerEmail;

    @NotBlank(message = "Buyer phone is required")
    private String buyerPhone;

    // Getters and Setters
    public String getShowId() { return showId; }
    public void setShowId(String showId) { this.showId = showId; }
    public String getTicketTypeId() { return ticketTypeId; }
    public void setTicketTypeId(String ticketTypeId) { this.ticketTypeId = ticketTypeId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
    public String getBuyerEmail() { return buyerEmail; }
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }
    public String getBuyerPhone() { return buyerPhone; }
    public void setBuyerPhone(String buyerPhone) { this.buyerPhone = buyerPhone; }
}