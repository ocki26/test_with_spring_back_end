package com.app_language.hoctiengtrung_online.Ticket.service;



import com.app_language.hoctiengtrung_online.Ticket.dto.CompanyDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Company;
import com.app_language.hoctiengtrung_online.Ticket.model.UserTicket;
import com.app_language.hoctiengtrung_online.Ticket.repository.CompanyRepository;
import com.app_language.hoctiengtrung_online.Ticket.repository.UserTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserTicketRepository userTicketRepository;

    // Dùng Transactional để đảm bảo cả 2 lưu thành công hoặc cùng thất bại
    @Transactional
    public Company createCompany(CompanyDTO dto) {
        // 1. Tạo Company
        Company company = new Company();
        company.setCode(dto.getCode());
        company.setName(dto.getName());
        company.setAddress(dto.getAddress());
        company.setEmail(dto.getEmail());
        company.setPhone(dto.getPhone());
        company.setDescription(dto.getDescription());

        Company savedCompany = companyRepository.save(company);

        // 2. Tạo tài khoản User cho công ty đăng nhập
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            UserTicket user = new UserTicket();
            user.setUsername(dto.getCode()); // Lấy mã công ty làm tên đăng nhập
            user.setPassword(dto.getPassword()); // Lưu ý: Thực tế cần mã hóa BCrypt
            user.setFullName(dto.getName());
            user.setRole(UserTicket.UserRole.COMPANY_USER);
            user.setCompanyCode(dto.getCode());
            user.setActive(true);

            userTicketRepository.save(user);
        }

        return savedCompany;
    }
}