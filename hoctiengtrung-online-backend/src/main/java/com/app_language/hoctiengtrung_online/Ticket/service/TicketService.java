package com.app_language.hoctiengtrung_online.Ticket.service;

import com.app_language.hoctiengtrung_online.Ticket.dto.BookingRequestDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Show;
import com.app_language.hoctiengtrung_online.Ticket.model.Ticket;
import com.app_language.hoctiengtrung_online.Ticket.model.TicketType;
import com.app_language.hoctiengtrung_online.Ticket.repository.ShowRepository;
import com.app_language.hoctiengtrung_online.Ticket.repository.TicketRepository;
import com.app_language.hoctiengtrung_online.Ticket.util.TicketCodeGenerator;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ShowRepository showRepository;
    private final MongoTemplate mongoTemplate;
    private final TicketCodeGenerator ticketCodeGenerator;

    public List<Ticket> bookTicket(BookingRequestDTO request) {
        log.info("Booking tickets - show: {}, type: {}, quantity: {}",
                request.getShowId(), request.getTicketTypeId(), request.getQuantity());

        // 1. Validate request
        validateBookingRequest(request);

        try {
            // 2. Tìm show và kiểm tra
            Show show = showRepository.findById(request.getShowId())
                    .orElseThrow(() -> new RuntimeException("Show không tồn tại với ID: " + request.getShowId()));

            // 3. Tìm ticket type và kiểm tra số lượng
            TicketType ticketType = show.getTicketTypes().stream()
                    .filter(type -> type.getId().equals(request.getTicketTypeId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Ticket type không tồn tại với ID: " + request.getTicketTypeId()));

            if (ticketType.getAvailableQuantity() < request.getQuantity()) {
                throw new RuntimeException("Không đủ vé. Số lượng còn lại: " + ticketType.getAvailableQuantity());
            }

            // 4. Reserve tickets atomically - Sửa query
            boolean reservationSuccess = reserveTicketsAtomic(request);
            if (!reservationSuccess) {
                throw new RuntimeException("Không thể đặt vé. Có thể đã có người khác đặt trước.");
            }

            // 5. Lấy lại show mới nhất sau khi update
            Show updatedShow = showRepository.findById(request.getShowId())
                    .orElseThrow(() -> new RuntimeException("Show không tồn tại sau khi update"));

            // 6. Create tickets
            List<Ticket> tickets = createTickets(request, updatedShow);

            // 7. Save tickets
            List<Ticket> savedTickets = ticketRepository.saveAll(tickets);

            log.info("Successfully booked {} tickets for show {}", savedTickets.size(), show.getId());
            return savedTickets;

        } catch (Exception e) {
            log.error("Failed to book tickets - show: {}, error: {}",
                    request.getShowId(), e.getMessage(), e);
            throw e;
        }
    }

    private boolean reserveTicketsAtomic(BookingRequestDTO request) {
        try {
            Query query = Query.query(
                    Criteria.where("_id").is(request.getShowId())
                            .and("ticketTypes._id").is(request.getTicketTypeId())
                            .and("ticketTypes.availableQuantity").gte(request.getQuantity())
            );

            Update update = new Update()
                    .inc("ticketTypes.$.availableQuantity", -request.getQuantity())
                    .inc("ticketTypes.$.soldQuantity", request.getQuantity());

            UpdateResult result = mongoTemplate.updateFirst(query, update, Show.class);
            return result.getModifiedCount() > 0;

        } catch (Exception e) {
            log.error("Error in atomic reservation: {}", e.getMessage());
            return false;
        }
    }

    private void validateBookingRequest(BookingRequestDTO request) {
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Số lượng vé phải lớn hơn 0");
        }
        if (request.getQuantity() > 10) {
            throw new IllegalArgumentException("Mỗi lần đặt tối đa 10 vé");
        }
    }

    private List<Ticket> createTickets(BookingRequestDTO request, Show show) {
        // Tìm ticket type để lấy thông tin
        TicketType ticketType = show.getTicketTypes().stream()
                .filter(type -> type.getId().equals(request.getTicketTypeId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ticket type không tồn tại"));

        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < request.getQuantity(); i++) {
            Ticket ticket = new Ticket();
            ticket.setShowId(show.getId());
            ticket.setShowName(show.getName());
            ticket.setTicketTypeId(ticketType.getId());
            ticket.setTicketTypeName(ticketType.getName());
            ticket.setPrice(ticketType.getPrice());
            ticket.setBuyerName(request.getBuyerName());
            ticket.setBuyerEmail(request.getBuyerEmail());
            ticket.setBuyerPhone(request.getBuyerPhone());
            ticket.setSoldBy(request.getSoldBy());
            ticket.setTicketCode(ticketCodeGenerator.generate());

            tickets.add(ticket);
        }
        return tickets;
    }

    // Các method khác giữ nguyên...
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getTicketsByShow(String showId) {
        return ticketRepository.findByShowId(showId);
    }

    public Optional<Ticket> getTicketByCode(String ticketCode) {
        return ticketRepository.findByTicketCode(ticketCode);
    }
}