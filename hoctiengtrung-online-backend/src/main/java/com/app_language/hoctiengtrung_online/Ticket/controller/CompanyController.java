package com.app_language.hoctiengtrung_online.Ticket.controller;



import com.app_language.hoctiengtrung_online.Ticket.dto.CompanyDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Company;
import com.app_language.hoctiengtrung_online.Ticket.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // API Tạo công ty (Admin dùng)
    // POST: http://localhost:8080/api/companies
    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody CompanyDTO companyDTO) {
        Company newCompany = companyService.createCompany(companyDTO);
        return ResponseEntity.ok(newCompany);
    }
}