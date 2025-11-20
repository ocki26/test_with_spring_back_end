package com.app_language.hoctiengtrung_online.Ticker.service;



import com.app_language.hoctiengtrung_online.Ticker.dto.TicketPurchaseRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.TicketScanRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.TicketPurchaseResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Ticket;
import java.util.List;

public interface TicketService {
    TicketPurchaseResponseDTO purchaseTickets(TicketPurchaseRequestDTO purchaseRequestDTO);
    Ticket scanTicket(TicketScanRequestDTO scanRequestDTO);
    Ticket getTicketById(String id);
    Ticket getTicketByCode(String ticketCode);
    List<Ticket> getTicketsByShow(String showId);
    List<Ticket> getTicketsByUser(String email);
    List<Ticket> getTicketsByType(String ticketTypeId);
    List<Ticket> getTicketsByStatus(String status);
    Ticket cancelTicket(String ticketCode);
    Object getTicketStats(String showId);
    List<Ticket> getScannedTicketsByShow(String showId);
    List<Ticket> getConfirmedTicketsByShow(String showId);
    List<Ticket> getCancelledTicketsByShow(String showId);
}