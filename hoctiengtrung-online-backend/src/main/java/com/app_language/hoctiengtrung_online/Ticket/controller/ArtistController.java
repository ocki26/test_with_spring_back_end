package com.app_language.hoctiengtrung_online.Ticket.controller;

import com.app_language.hoctiengtrung_online.Ticket.dto.ArtistDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Artist;
import com.app_language.hoctiengtrung_online.Ticket.service.ArtistService;
import com.fasterxml.jackson.databind.ObjectMapper; // Import thêm
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/artists")

public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ObjectMapper objectMapper; // Dùng để chuyển String -> Object

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createArtist(
            // 1. Đổi ArtistDTO thành String để tránh lỗi 415
            @RequestPart("artist") String artistString,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            // 2. Convert thủ công từ String JSON sang Object
            ArtistDTO artistDTO = objectMapper.readValue(artistString, ArtistDTO.class);

            // Debug log
            System.out.println("=== DEBUG MULTIPART REQUEST ===");
            System.out.println("Artist JSON: " + artistString);
            System.out.println("Image: " + (image != null ? image.getOriginalFilename() : "null"));

            // 3. Gọi service như bình thường
            Artist newArtist = artistService.createArtist(artistDTO, image);
            return ResponseEntity.ok(newArtist);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error processing request: " + e.getMessage());
        }
    }
}