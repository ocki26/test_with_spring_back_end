package com.app_language.hoctiengtrung_online.Ticket.service;



import com.app_language.hoctiengtrung_online.Ticket.dto.BookingRequestDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Show;
import com.app_language.hoctiengtrung_online.Ticket.model.Ticket;
import com.app_language.hoctiengtrung_online.Ticket.model.TicketType;
import com.app_language.hoctiengtrung_online.Ticket.repository.ShowRepository;
import com.app_language.hoctiengtrung_online.Ticket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowRepository showRepository;

    @Transactional // Đảm bảo tính toàn vẹn dữ liệu
    public List<Ticket> bookTicket(BookingRequestDTO request) {
        // 1. Lấy thông tin Show
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new RuntimeException("Show không tồn tại"));

        // 2. Tìm loại vé người dùng muốn mua trong Show
        TicketType selectedType = null;
        for (TicketType type : show.getTicketTypes()) {
            if (type.getId().equals(request.getTicketTypeId())) {
                selectedType = type;
                break;
            }
        }

        if (selectedType == null) {
            throw new RuntimeException("Loại vé không tồn tại trong show này");
        }

        // 3. Kiểm tra số lượng vé còn đủ không
        if (selectedType.getAvailableQuantity() < request.getQuantity()) {
            throw new RuntimeException("Vé đã hết hoặc không đủ số lượng yêu cầu");
        }

        // 4. Thực hiện mua vé (Trừ kho và tạo vé)
        List<Ticket> purchasedTickets = new ArrayList<>();

        // Cập nhật số lượng trong Show object
        selectedType.setAvailableQuantity(selectedType.getAvailableQuantity() - request.getQuantity());
        selectedType.setSoldQuantity(selectedType.getSoldQuantity() + request.getQuantity());

        // Vòng lặp tạo N vé
        for (int i = 0; i < request.getQuantity(); i++) {
            Ticket ticket = new Ticket();
            ticket.setShowId(show.getId());
            ticket.setShowName(show.getName());
            ticket.setTicketTypeId(selectedType.getId());
            ticket.setTicketTypeName(selectedType.getName());
            ticket.setPrice(selectedType.getPrice());

            // Thông tin người mua (giống nhau cho tất cả vé trong lượt mua này)
            ticket.setBuyerName(request.getBuyerName());
            ticket.setBuyerEmail(request.getBuyerEmail());
            ticket.setBuyerPhone(request.getBuyerPhone());
            ticket.setSoldBy(request.getSoldBy()); // CTV giới thiệu

            ticket.setStatus("CONFIRMED"); // Giả sử mua thành công luôn

            purchasedTickets.add(ticket);
        }

        // 5. Lưu dữ liệu
        showRepository.save(show); // Lưu lại show với số lượng vé mới
        ticketRepository.saveAll(purchasedTickets); // Lưu danh sách vé mới tạo

        return purchasedTickets;
    }
}