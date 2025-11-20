package com.app_language.hoctiengtrung_online.Ticker.dto.Response;



import java.util.List;

public class TicketPurchaseResponseDTO {
    private boolean success;
    private String message;
    private List<TicketResponseDTO> tickets;
    private int quantity;
    private String showName;
    private String totalAmount;

    public TicketPurchaseResponseDTO() {}

    public TicketPurchaseResponseDTO(boolean success, String message, List<TicketResponseDTO> tickets,
                                     int quantity, String showName, String totalAmount) {
        this.success = success;
        this.message = message;
        this.tickets = tickets;
        this.quantity = quantity;
        this.showName = showName;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<TicketResponseDTO> getTickets() { return tickets; }
    public void setTickets(List<TicketResponseDTO> tickets) { this.tickets = tickets; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getShowName() { return showName; }
    public void setShowName(String showName) { this.showName = showName; }
    public String getTotalAmount() { return totalAmount; }
    public void setTotalAmount(String totalAmount) { this.totalAmount = totalAmount; }
}