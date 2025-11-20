package com.app_language.hoctiengtrung_online.Ticket.controller;

import com.app_language.hoctiengtrung_online.Ticket.dto.BookingRequestDTO;
import com.app_language.hoctiengtrung_online.Ticket.dto.ErrorResponse;
import com.app_language.hoctiengtrung_online.Ticket.dto.TicketDTO;
import com.app_language.hoctiengtrung_online.Ticket.mapper.TicketMapper;
import com.app_language.hoctiengtrung_online.Ticket.model.Ticket;
import com.app_language.hoctiengtrung_online.Ticket.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @PostMapping("/book")
    public ResponseEntity<?> bookTickets(@Valid @RequestBody BookingRequestDTO bookingRequest) {
        try {
            List<Ticket> tickets = ticketService.bookTicket(bookingRequest);
            List<TicketDTO> ticketDTOs = ticketMapper.toDTOList(tickets);
            return ResponseEntity.ok(ticketDTOs);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid booking request: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("INVALID_REQUEST", e.getMessage()));
        } catch (RuntimeException e) {
            log.error("Booking failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("BOOKING_FAILED", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        List<TicketDTO> ticketDTOs = ticketMapper.toDTOList(tickets);
        return ResponseEntity.ok(ticketDTOs);
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<TicketDTO>> getTicketsByShow(@PathVariable String showId) {
        List<Ticket> tickets = ticketService.getTicketsByShow(showId);
        List<TicketDTO> ticketDTOs = ticketMapper.toDTOList(tickets);
        return ResponseEntity.ok(ticketDTOs);
    }

    @GetMapping("/{ticketCode}")
    public ResponseEntity<TicketDTO> getTicketByCode(@PathVariable String ticketCode) {
        Optional<Ticket> ticket = ticketService.getTicketByCode(ticketCode);
        if (ticket.isPresent()) {
            TicketDTO ticketDTO = ticketMapper.toDTO(ticket.get());
            return ResponseEntity.ok(ticketDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}