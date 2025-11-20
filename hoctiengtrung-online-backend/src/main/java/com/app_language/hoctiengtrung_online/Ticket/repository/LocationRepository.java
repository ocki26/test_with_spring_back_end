package com.app_language.hoctiengtrung_online.Ticket.repository;


import com.app_language.hoctiengtrung_online.Ticket.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepository extends MongoRepository<Location, String> {
}
