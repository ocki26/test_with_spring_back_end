package com.app_language.hoctiengtrung_online.Ticket.service;

import com.app_language.hoctiengtrung_online.Ticket.dto.ShowDTO;
import com.app_language.hoctiengtrung_online.Ticket.dto.TicketTypeDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Location;
import com.app_language.hoctiengtrung_online.Ticket.model.Show;
import com.app_language.hoctiengtrung_online.Ticket.model.TicketType;
import com.app_language.hoctiengtrung_online.Ticket.repository.LocationRepository;
import com.app_language.hoctiengtrung_online.Ticket.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService {

    @Autowired
    public ShowRepository showRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public Show createShow(ShowDTO dto, List<MultipartFile> images) {
        Show show = new Show();
        show.setName(dto.getName());
        show.setDescription(dto.getDescription());
        show.setGenre(dto.getGenre());
        show.setStartTime(dto.getStartTime());
        show.setEndTime(dto.getEndTime());
        show.setCompanyId(dto.getCompanyId());
        show.setArtistIds(dto.getArtistIds());

        // 1. Xử lý Location: Tìm địa điểm để lấy địa chỉ
        if (dto.getLocationId() != null) {
            Location loc = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy địa điểm ID: " + dto.getLocationId()));
            show.setLocationId(loc.getId());
            show.setLocationAddress(loc.getAddress()); // Điền tự động địa chỉ
        }

        // 2. Xử lý Upload nhiều ảnh
        List<String> imageUrls = new ArrayList<>();
        if (images != null) {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    String url = fileStorageService.storeFile(file);
                    imageUrls.add(url);
                }
            }
        }
        show.setImageUrls(imageUrls);

        // 3. Xử lý Loại vé (Ticket Types)
        List<TicketType> ticketTypes = new ArrayList<>();
        if (dto.getTicketTypes() != null) {
            for (TicketTypeDTO typeDto : dto.getTicketTypes()) {
                TicketType type = new TicketType();
                type.setName(typeDto.getName());
                type.setDescription(typeDto.getDescription());
                type.setPrice(typeDto.getPrice());
                type.setQuantity(typeDto.getQuantity());
                type.setAvailableQuantity(typeDto.getQuantity()); // Ban đầu còn full
                type.setSoldQuantity(0);
                type.setActive(true);
                ticketTypes.add(type);
            }
        }
        show.setTicketTypes(ticketTypes);

        return showRepository.save(show);
    }

    public Show getShowById(String id) {
        return showRepository.findById(id).orElse(null);
    }

    // THÊM METHOD NÀY
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    // THÊM METHOD NÀY
    public List<Show> getShowsByCompanyId(String companyId) {
        return showRepository.findByCompanyId(companyId);
    }

    // THÊM METHOD UPDATE (tùy chọn)
    public Show updateShow(String id, ShowDTO dto, List<MultipartFile> images) {
        Optional<Show> existingShow = showRepository.findById(id);
        if (existingShow.isPresent()) {
            Show show = existingShow.get();
            show.setName(dto.getName());
            show.setDescription(dto.getDescription());
            show.setGenre(dto.getGenre());
            show.setStartTime(dto.getStartTime());
            show.setEndTime(dto.getEndTime());
            show.setArtistIds(dto.getArtistIds());

            // Update location if provided
            if (dto.getLocationId() != null) {
                Location loc = locationRepository.findById(dto.getLocationId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy địa điểm ID: " + dto.getLocationId()));
                show.setLocationId(loc.getId());
                show.setLocationAddress(loc.getAddress());
            }

            // Update images if provided
            if (images != null && !images.isEmpty()) {
                List<String> imageUrls = new ArrayList<>();
                for (MultipartFile file : images) {
                    if (!file.isEmpty()) {
                        String url = fileStorageService.storeFile(file);
                        imageUrls.add(url);
                    }
                }
                show.setImageUrls(imageUrls);
            }

            return showRepository.save(show);
        }
        throw new RuntimeException("Show not found with id: " + id);
    }
}