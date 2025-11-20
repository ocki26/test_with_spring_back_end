package com.app_language.hoctiengtrung_online.Ticker.service.lmpl;



import com.app_language.hoctiengtrung_online.Ticker.dto.CompanyLoginRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.CompanyRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Company;
import com.app_language.hoctiengtrung_online.Ticker.repository.CompanyRepository;
import com.app_language.hoctiengtrung_online.Ticker.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Company createCompany(CompanyRequestDTO companyRequestDTO) {
        // Check if company code already exists
        if (existsByCode(companyRequestDTO.getCode())) {
            throw new RuntimeException("Company code already exists: " + companyRequestDTO.getCode());
        }

        // Check if email already exists
        if (existsByEmail(companyRequestDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + companyRequestDTO.getEmail());
        }

        Company company = new Company();
        company.setCode(companyRequestDTO.getCode());
        company.setName(companyRequestDTO.getName());
        company.setDescription(companyRequestDTO.getDescription());
        company.setAddress(companyRequestDTO.getAddress());
        company.setPhone(companyRequestDTO.getPhone());
        company.setEmail(companyRequestDTO.getEmail());
        company.setPassword(companyRequestDTO.getPassword()); // In production, encrypt this!
        company.setActive(true);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        return companyRepository.save(company);
    }

    @Override
    public Company updateCompany(String id, CompanyRequestDTO companyRequestDTO) {
        Optional<Company> companyOpt = companyRepository.findById(id);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();

            // Check if code is being changed and conflicts with existing company
            if (!company.getCode().equals(companyRequestDTO.getCode()) &&
                    existsByCode(companyRequestDTO.getCode())) {
                throw new RuntimeException("Company code already exists: " + companyRequestDTO.getCode());
            }

            // Check if email is being changed and conflicts with existing company
            if (!company.getEmail().equals(companyRequestDTO.getEmail()) &&
                    existsByEmail(companyRequestDTO.getEmail())) {
                throw new RuntimeException("Email already exists: " + companyRequestDTO.getEmail());
            }

            company.setCode(companyRequestDTO.getCode());
            company.setName(companyRequestDTO.getName());
            company.setDescription(companyRequestDTO.getDescription());
            company.setAddress(companyRequestDTO.getAddress());
            company.setPhone(companyRequestDTO.getPhone());
            company.setEmail(companyRequestDTO.getEmail());
            if (companyRequestDTO.getPassword() != null && !companyRequestDTO.getPassword().isEmpty()) {
                company.setPassword(companyRequestDTO.getPassword()); // Encrypt in production!
            }
            company.setUpdatedAt(LocalDateTime.now());

            return companyRepository.save(company);
        }
        throw new RuntimeException("Company not found with id: " + id);
    }

    @Override
    public void deleteCompany(String id) {
        Optional<Company> companyOpt = companyRepository.findById(id);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            company.setActive(false);
            company.setUpdatedAt(LocalDateTime.now());
            companyRepository.save(company);
        } else {
            throw new RuntimeException("Company not found with id: " + id);
        }
    }

    @Override
    public Company getCompanyById(String id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    @Override
    public Company getCompanyByCode(String code) {
        return companyRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Company not found with code: " + code));
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public List<Company> getActiveCompanies() {
        return companyRepository.findByActiveTrue();
    }

    @Override
    public Company login(CompanyLoginRequestDTO loginRequestDTO) {
        return companyRepository.findByCodeAndPassword(loginRequestDTO.getCode(), loginRequestDTO.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid company code or password"));
    }

    @Override
    public Company activateCompany(String id) {
        Optional<Company> companyOpt = companyRepository.findById(id);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            company.setActive(true);
            company.setUpdatedAt(LocalDateTime.now());
            return companyRepository.save(company);
        }
        throw new RuntimeException("Company not found with id: " + id);
    }

    @Override
    public Company deactivateCompany(String id) {
        Optional<Company> companyOpt = companyRepository.findById(id);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            company.setActive(false);
            company.setUpdatedAt(LocalDateTime.now());
            return companyRepository.save(company);
        }
        throw new RuntimeException("Company not found with id: " + id);
    }

    @Override
    public List<Company> searchCompanies(String keyword) {
        return companyRepository.searchActiveCompanies(keyword);
    }

    @Override
    public boolean existsByCode(String code) {
        return companyRepository.existsByCode(code);
    }

    @Override
    public boolean existsByEmail(String email) {
        return companyRepository.findByEmail(email).isPresent();
    }
}