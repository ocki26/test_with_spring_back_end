package com.app_language.hoctiengtrung_online.Ticker.controller;



import com.app_language.hoctiengtrung_online.Ticker.dto.TicketPurchaseRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.TicketScanRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ApiResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.TicketPurchaseResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.TicketResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Ticket;
import com.app_language.hoctiengtrung_online.Ticker.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Ticket Management", description = "APIs for managing tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/purchase")
    @Operation(summary = "Purchase tickets")
    public ResponseEntity<ApiResponseDTO> purchaseTickets(@Valid @RequestBody TicketPurchaseRequestDTO purchaseRequestDTO) {
        TicketPurchaseResponseDTO response = ticketService.purchaseTickets(purchaseRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Tickets purchased successfully", response));
    }

    @PostMapping("/scan")
    @Operation(summary = "Scan ticket for entry")
    public ResponseEntity<ApiResponseDTO> scanTicket(@Valid @RequestBody TicketScanRequestDTO scanRequestDTO) {
        Ticket ticket = ticketService.scanTicket(scanRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Ticket scanned successfully", ticket));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID")
    public ResponseEntity<ApiResponseDTO> getTicketById(@PathVariable String id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Ticket retrieved successfully", ticket));
    }

    @GetMapping("/code/{ticketCode}")
    @Operation(summary = "Get ticket by code")
    public ResponseEntity<ApiResponseDTO> getTicketByCode(@PathVariable String ticketCode) {
        Ticket ticket = ticketService.getTicketByCode(ticketCode);
        return ResponseEntity.ok(ApiResponseDTO.success("Ticket retrieved successfully", ticket));
    }

    @GetMapping("/show/{showId}")
    @Operation(summary = "Get tickets by show")
    public ResponseEntity<ApiResponseDTO> getTicketsByShow(@PathVariable String showId) {
        List<Ticket> tickets = ticketService.getTicketsByShow(showId);
        return ResponseEntity.ok(ApiResponseDTO.success("Tickets retrieved successfully", tickets));
    }

    @GetMapping("/user/{email}")
    @Operation(summary = "Get tickets by user email")
    public ResponseEntity<ApiResponseDTO> getTicketsByUser(@PathVariable String email) {
        List<Ticket> tickets = ticketService.getTicketsByUser(email);
        return ResponseEntity.ok(ApiResponseDTO.success("Tickets retrieved successfully", tickets));
    }

    @GetMapping("/type/{ticketTypeId}")
    @Operation(summary = "Get tickets by ticket type")
    public ResponseEntity<ApiResponseDTO> getTicketsByType(@PathVariable String ticketTypeId) {
        List<Ticket> tickets = ticketService.getTicketsByType(ticketTypeId);
        return ResponseEntity.ok(ApiResponseDTO.success("Tickets retrieved successfully", tickets));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get tickets by status")
    public ResponseEntity<ApiResponseDTO> getTicketsByStatus(@PathVariable String status) {
        List<Ticket> tickets = ticketService.getTicketsByStatus(status);
        return ResponseEntity.ok(ApiResponseDTO.success("Tickets retrieved successfully", tickets));
    }

    @PutMapping("/{ticketCode}/cancel")
    @Operation(summary = "Cancel a ticket")
    public ResponseEntity<ApiResponseDTO> cancelTicket(@PathVariable String ticketCode) {
        Ticket ticket = ticketService.cancelTicket(ticketCode);
        return ResponseEntity.ok(ApiResponseDTO.success("Ticket cancelled successfully", ticket));
    }

    @GetMapping("/stats/show/{showId}")
    @Operation(summary = "Get ticket statistics for a show")
    public ResponseEntity<ApiResponseDTO> getTicketStats(@PathVariable String showId) {
        Object stats = ticketService.getTicketStats(showId);
        return ResponseEntity.ok(ApiResponseDTO.success("Ticket statistics retrieved successfully", stats));
    }
}