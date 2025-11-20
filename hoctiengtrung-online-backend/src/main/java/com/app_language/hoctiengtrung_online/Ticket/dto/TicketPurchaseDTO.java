package com.app_language.hoctiengtrung_online.Ticket.dto;



public class TicketPurchaseDTO {
    private String showId;
    private String ticketTypeId;
    private Integer quantity;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;

    // Constructors
    public TicketPurchaseDTO() {}

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