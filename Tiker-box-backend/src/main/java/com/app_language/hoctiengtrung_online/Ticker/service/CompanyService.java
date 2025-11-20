package com.app_language.hoctiengtrung_online.Ticker.service;



import com.app_language.hoctiengtrung_online.Ticker.dto.CompanyLoginRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.CompanyRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Company;
import java.util.List;

public interface CompanyService {
    Company createCompany(CompanyRequestDTO companyRequestDTO);
    Company updateCompany(String id, CompanyRequestDTO companyRequestDTO);
    void deleteCompany(String id);
    Company getCompanyById(String id);
    Company getCompanyByCode(String code);
    List<Company> getAllCompanies();
    List<Company> getActiveCompanies();
    Company login(CompanyLoginRequestDTO loginRequestDTO);
    Company activateCompany(String id);
    Company deactivateCompany(String id);
    List<Company> searchCompanies(String keyword);
    boolean existsByCode(String code);
    boolean existsByEmail(String email);
}