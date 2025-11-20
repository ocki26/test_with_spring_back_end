package com.app_language.hoctiengtrung_online.Ticket.repository;


import com.app_language.hoctiengtrung_online.Ticket.model.UserTicket;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserTicketRepository extends MongoRepository<UserTicket, String> {
    Optional<UserTicket> findByUsername(String username);
}