package com.app_language.hoctiengtrung_online.Ticket.repository;


import com.app_language.hoctiengtrung_online.Ticket.model.Show;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ShowRepository extends MongoRepository<Show, String> {
    List<Show> findByCompanyId(String companyId);
}