// ArtistService.java
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

    // XÓA: @Autowired private FileStorageService fileStorageService;

    public Artist createArtist(ArtistDTO dto, MultipartFile imageFile) {
        try {
            Artist artist = new Artist();
            artist.setName(dto.getName());
            artist.setDescription(dto.getDescription());
            artist.setCompanyCode(dto.getCompanyCode());

            // THAY ĐỔI: Xử lý file ảnh - lưu binary data
            if (imageFile != null && !imageFile.isEmpty()) {
                // Validate file size (tối đa 15MB)
                if (imageFile.getSize() > 15 * 1024 * 1024) {
                    throw new RuntimeException("File size too large. Maximum is 15MB");
                }

                // Validate content type
                String contentType = imageFile.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new RuntimeException("Only image files are allowed");
                }

                artist.setImageData(imageFile.getBytes());
                artist.setImageContentType(contentType);
                artist.setImageFileName(imageFile.getOriginalFilename());
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