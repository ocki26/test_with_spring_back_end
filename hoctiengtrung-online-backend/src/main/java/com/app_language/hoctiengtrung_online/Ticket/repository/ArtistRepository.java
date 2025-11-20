package com.app_language.hoctiengtrung_online.Ticket.repository;


import com.app_language.hoctiengtrung_online.Ticket.model.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ArtistRepository extends MongoRepository<Artist, String> {
    List<Artist> findByCompanyCode(String companyCode);
}