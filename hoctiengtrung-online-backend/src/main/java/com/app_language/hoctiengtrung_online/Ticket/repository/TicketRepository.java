package com.app_language.hoctiengtrung_online.Ticket.repository;


import com.app_language.hoctiengtrung_online.Ticket.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    Optional<Ticket> findByTicketCode(String ticketCode);
    List<Ticket> findByShowId(String showId);
    List<Ticket> findByBuyerEmail(String email);
}