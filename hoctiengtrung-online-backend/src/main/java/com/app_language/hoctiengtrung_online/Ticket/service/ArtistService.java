package com.app_language.hoctiengtrung_online.Ticket.service;

import com.app_language.hoctiengtrung_online.Ticket.dto.ArtistDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Artist;
import com.app_language.hoctiengtrung_online.Ticket.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public Artist createArtist(ArtistDTO dto, MultipartFile imageFile) {
        try {
            Artist artist = new Artist();
            artist.setName(dto.getName());
            artist.setDescription(dto.getDescription());
            artist.setCompanyCode(dto.getCompanyCode());

            // Xử lý file ảnh
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = fileStorageService.storeFile(imageFile);
                artist.setImageUrl(imageUrl);
            } else if (dto.getImageUrl() != null && !dto.getImageUrl().isEmpty()) {
                // Nếu không có file nhưng có URL từ DTO
                artist.setImageUrl(dto.getImageUrl());
            }

            return artistRepository.save(artist);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create artist: " + e.getMessage(), e);
        }
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Optional<Artist> getArtistById(String id) {
        return artistRepository.findById(id);
    }

    public List<Artist> getArtistsByCompanyCode(String companyCode) {
        return artistRepository.findByCompanyCode(companyCode);
    }
}