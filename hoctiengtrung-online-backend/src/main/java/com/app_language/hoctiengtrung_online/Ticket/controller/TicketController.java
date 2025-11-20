package com.app_language.hoctiengtrung_online.Ticket.controller;



import com.app_language.hoctiengtrung_online.Ticket.dto.BookingRequestDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Ticket;
import com.app_language.hoctiengtrung_online.Ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // Mua vé
    @PostMapping("/book")
    public ResponseEntity<?> bookTickets(@RequestBody BookingRequestDTO bookingRequest) {
        try {
            List<Ticket> tickets = ticketService.bookTicket(bookingRequest);
            return ResponseEntity.ok(tickets);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}