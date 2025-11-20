package com.app_language.hoctiengtrung_online.Ticker.service.lmpl;


import com.app_language.hoctiengtrung_online.Ticker.dto.TicketPurchaseRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.TicketScanRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.TicketPurchaseResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.TicketResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Show;
import com.app_language.hoctiengtrung_online.Ticker.model.Ticket;
import com.app_language.hoctiengtrung_online.Ticker.model.TicketType;
import com.app_language.hoctiengtrung_online.Ticker.repository.ShowRepository;
import com.app_language.hoctiengtrung_online.Ticker.repository.TicketRepository;
import com.app_language.hoctiengtrung_online.Ticker.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowRepository showRepository;

    @Override
    @Transactional
    public TicketPurchaseResponseDTO purchaseTickets(TicketPurchaseRequestDTO purchaseRequestDTO) {
        // Find the show
        Show show = showRepository.findById(purchaseRequestDTO.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + purchaseRequestDTO.getShowId()));

        // Find the ticket type in the show
        TicketType ticketType = null;
        int ticketTypeIndex = -1;

        List<TicketType> ticketTypes = show.getTicketTypes();
        for (int i = 0; i < ticketTypes.size(); i++) {
            TicketType type = ticketTypes.get(i);
            if (type.getId().equals(purchaseRequestDTO.getTicketTypeId())) {
                ticketType = type;
                ticketTypeIndex = i;
                break;
            }
        }

        if (ticketType == null) {
            throw new RuntimeException("Ticket type not found with id: " + purchaseRequestDTO.getTicketTypeId());
        }

        // Check available quantity
        if (ticketType.getAvailableQuantity() < purchaseRequestDTO.getQuantity()) {
            throw new RuntimeException("Not enough tickets available. Available: " + ticketType.getAvailableQuantity() + ", Requested: " + purchaseRequestDTO.getQuantity());
        }

        // Update available quantity and sold quantity
        ticketType.setAvailableQuantity(ticketType.getAvailableQuantity() - purchaseRequestDTO.getQuantity());
        ticketType.setSoldQuantity(ticketType.getSoldQuantity() + purchaseRequestDTO.getQuantity());

        // Update the ticket type in the show
        ticketTypes.set(ticketTypeIndex, ticketType);
        show.setTicketTypes(ticketTypes);

        // Create tickets
        List<Ticket> tickets = new ArrayList<>();
        BigDecimal totalAmount = ticketType.getPrice().multiply(BigDecimal.valueOf(purchaseRequestDTO.getQuantity()));

        for (int i = 0; i < purchaseRequestDTO.getQuantity(); i++) {
            Ticket ticket = new Ticket();
            ticket.setShowId(show.getId());
            ticket.setShowName(show.getName());
            ticket.setTicketTypeId(ticketType.getId());
            ticket.setTicketTypeName(ticketType.getName());
            ticket.setPrice(ticketType.getPrice());
            ticket.setBuyerName(purchaseRequestDTO.getBuyerName());
            ticket.setBuyerEmail(purchaseRequestDTO.getBuyerEmail());
            ticket.setBuyerPhone(purchaseRequestDTO.getBuyerPhone());
            ticket.setStatus("CONFIRMED");
            ticket.setPurchaseTime(LocalDateTime.now());
            ticket.setCreatedAt(LocalDateTime.now());
            tickets.add(ticket);
        }

        // Save tickets and update show
        List<Ticket> savedTickets = ticketRepository.saveAll(tickets);
        showRepository.save(show);

        // Convert to response DTOs
        List<TicketResponseDTO> ticketResponseDTOs = savedTickets.stream().map(ticket -> {
            TicketResponseDTO dto = new TicketResponseDTO();
            dto.setId(ticket.getId());
            dto.setTicketCode(ticket.getTicketCode());
            dto.setShowId(ticket.getShowId());
            dto.setShowName(ticket.getShowName());
            dto.setTicketTypeId(ticket.getTicketTypeId());
            dto.setTicketTypeName(ticket.getTicketTypeName());
            dto.setPrice(ticket.getPrice());
            dto.setBuyerName(ticket.getBuyerName());
            dto.setBuyerEmail(ticket.getBuyerEmail());
            dto.setBuyerPhone(ticket.getBuyerPhone());
            dto.setStatus(ticket.getStatus());
            dto.setPurchaseTime(ticket.getPurchaseTime());
            dto.setCreatedAt(ticket.getCreatedAt());
            dto.setScannedBy(ticket.getScannedBy());
            dto.setSoldBy(ticket.getSoldBy());
            return dto;
        }).collect(Collectors.toList());

        return new TicketPurchaseResponseDTO(
                true,
                "Tickets purchased successfully",
                ticketResponseDTOs,
                purchaseRequestDTO.getQuantity(),
                show.getName(),
                totalAmount.toString()
        );
    }

    @Override
    public Ticket scanTicket(TicketScanRequestDTO scanRequestDTO) {
        Ticket ticket = ticketRepository.findByTicketCode(scanRequestDTO.getTicketCode())
                .orElseThrow(() -> new RuntimeException("Ticket not found with code: " + scanRequestDTO.getTicketCode()));

        if (!"CONFIRMED".equals(ticket.getStatus())) {
            throw new RuntimeException("Ticket is not in CONFIRMED status. Current status: " + ticket.getStatus());
        }

        ticket.setStatus("SCANNED");
        ticket.setScannedBy(scanRequestDTO.getScannedBy());
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketById(String id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
    }

    @Override
    public Ticket getTicketByCode(String ticketCode) {
        return ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new RuntimeException("Ticket not found with code: " + ticketCode));
    }

    @Override
    public List<Ticket> getTicketsByShow(String showId) {
        return ticketRepository.findByShowId(showId);
    }

    @Override
    public List<Ticket> getTicketsByUser(String email) {
        return ticketRepository.findByBuyerEmail(email);
    }

    @Override
    public List<Ticket> getTicketsByType(String ticketTypeId) {
        return ticketRepository.findByTicketTypeId(ticketTypeId);
    }

    @Override
    public List<Ticket> getTicketsByStatus(String status) {
        return ticketRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Ticket cancelTicket(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new RuntimeException("Ticket not found with code: " + ticketCode));

        if (!"CONFIRMED".equals(ticket.getStatus())) {
            throw new RuntimeException("Only CONFIRMED tickets can be cancelled. Current status: " + ticket.getStatus());
        }

        ticket.setStatus("CANCELLED");

        // Find the show and ticket type to update available quantity
        Show show = showRepository.findById(ticket.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + ticket.getShowId()));

        List<TicketType> ticketTypes = show.getTicketTypes();
        for (int i = 0; i < ticketTypes.size(); i++) {
            TicketType type = ticketTypes.get(i);
            if (type.getId().equals(ticket.getTicketTypeId())) {
                type.setAvailableQuantity(type.getAvailableQuantity() + 1);
                type.setSoldQuantity(type.getSoldQuantity() - 1);
                ticketTypes.set(i, type);
                break;
            }
        }

        show.setTicketTypes(ticketTypes);
        showRepository.save(show);

        return ticketRepository.save(ticket);
    }

    @Override
    public Object getTicketStats(String showId) {
        long totalTickets = ticketRepository.countByShowId(showId);
        long confirmedTickets = ticketRepository.countByShowIdAndStatus(showId, "CONFIRMED");
        long scannedTickets = ticketRepository.countByShowIdAndStatus(showId, "SCANNED");
        long cancelledTickets = ticketRepository.countByShowIdAndStatus(showId, "CANCELLED");

        return new Object() {
            public final long total = totalTickets;
            public final long confirmed = confirmedTickets;
            public final long scanned = scannedTickets;
            public final long cancelled = cancelledTickets;
            public final double scannedRate = totalTickets > 0 ? (double) scannedTickets / totalTickets * 100 : 0;
        };
    }

    @Override
    public List<Ticket> getScannedTicketsByShow(String showId) {
        return ticketRepository.findScannedTicketsByShow(showId);
    }

    @Override
    public List<Ticket> getConfirmedTicketsByShow(String showId) {
        return ticketRepository.findConfirmedTicketsByShow(showId);
    }

    @Override
    public List<Ticket> getCancelledTicketsByShow(String showId) {
        return ticketRepository.findCancelledTicketsByShow(showId);
    }
}