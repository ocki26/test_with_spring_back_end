package com.app_language.hoctiengtrung_online.Ticket.repository;


import com.app_language.hoctiengtrung_online.Ticket.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CompanyRepository extends MongoRepository<Company, String> {
    Optional<Company> findByCode(String code);
}