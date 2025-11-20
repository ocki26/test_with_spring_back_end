package com.app_language.hoctiengtrung_online.Ticker.controller;



import com.app_language.hoctiengtrung_online.Ticker.dto.CompanyLoginRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.CompanyRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ApiResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.CompanyResponseDTO;
//import com.app_language.hoctiengtrung_online.Ticker.dto.Response.LoginResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Company;
import com.app_language.hoctiengtrung_online.Ticker.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Company Management", description = "APIs for managing companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    @Operation(summary = "Create a new company")
    public ResponseEntity<ApiResponseDTO> createCompany(@Valid @RequestBody CompanyRequestDTO companyRequestDTO) {
        Company company = companyService.createCompany(companyRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Company created successfully", company));
    }

    @PostMapping("/login")
    @Operation(summary = "Company login")
    public ResponseEntity<ApiResponseDTO> login(@Valid @RequestBody CompanyLoginRequestDTO loginRequestDTO) {
        Company company = companyService.login(loginRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Login successful", company));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a company")
    public ResponseEntity<ApiResponseDTO> updateCompany(@PathVariable String id,
                                                        @Valid @RequestBody CompanyRequestDTO companyRequestDTO) {
        Company company = companyService.updateCompany(id, companyRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Company updated successfully", company));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a company")
    public ResponseEntity<ApiResponseDTO> deleteCompany(@PathVariable String id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Company deleted successfully", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get company by ID")
    public ResponseEntity<ApiResponseDTO> getCompanyById(@PathVariable String id) {
        Company company = companyService.getCompanyById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Company retrieved successfully", company));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get company by code")
    public ResponseEntity<ApiResponseDTO> getCompanyByCode(@PathVariable String code) {
        Company company = companyService.getCompanyByCode(code);
        return ResponseEntity.ok(ApiResponseDTO.success("Company retrieved successfully", company));
    }

    @GetMapping
    @Operation(summary = "Get all companies")
    public ResponseEntity<ApiResponseDTO> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(ApiResponseDTO.success("Companies retrieved successfully", companies));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active companies")
    public ResponseEntity<ApiResponseDTO> getActiveCompanies() {
        List<Company> companies = companyService.getActiveCompanies();
        return ResponseEntity.ok(ApiResponseDTO.success("Active companies retrieved successfully", companies));
    }
}