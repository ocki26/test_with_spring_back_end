package com.app_language.hoctiengtrung_online.Ticker.dto;


import jakarta.validation.constraints.NotBlank;

public class TicketScanRequestDTO {

    @NotBlank(message = "Ticket code is required")
    private String ticketCode;

    @NotBlank(message = "Scanned by is required")
    private String scannedBy;

    private String scannerRole; // Có thể thêm role của người scan

    // Constructors
    public TicketScanRequestDTO() {}

    public TicketScanRequestDTO(String ticketCode, String scannedBy) {
        this.ticketCode = ticketCode;
        this.scannedBy = scannedBy;
    }

    public TicketScanRequestDTO(String ticketCode, String scannedBy, String scannerRole) {
        this.ticketCode = ticketCode;
        this.scannedBy = scannedBy;
        this.scannerRole = scannerRole;
    }

    // Getters and Setters
    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(String scannedBy) {
        this.scannedBy = scannedBy;
    }

    public String getScannerRole() {
        return scannerRole;
    }

    public void setScannerRole(String scannerRole) {
        this.scannerRole = scannerRole;
    }

    @Override
    public String toString() {
        return "TicketScanRequestDTO{" +
                "ticketCode='" + ticketCode + '\'' +
                ", scannedBy='" + scannedBy + '\'' +
                ", scannerRole='" + scannerRole + '\'' +
                '}';
    }
}