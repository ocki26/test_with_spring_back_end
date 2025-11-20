package com.app_language.hoctiengtrung_online.Ticket.controller;

import com.app_language.hoctiengtrung_online.Ticket.dto.ArtistDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Artist;
import com.app_language.hoctiengtrung_online.Ticket.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createArtist(
            @RequestPart("artist") @Valid ArtistDTO artistDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            System.out.println("Received ArtistDTO: " + artistDTO.toString());
            System.out.println("Image file: " + (image != null ? image.getOriginalFilename() : "No image"));

            Artist newArtist = artistService.createArtist(artistDTO, image);
            return ResponseEntity.ok(newArtist);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating artist: " + e.getMessage());
        }
    }

    // Thêm các endpoint khác
    @GetMapping
    public ResponseEntity<?> getAllArtists() {
        try {
            return ResponseEntity.ok(artistService.getAllArtists());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving artists: " + e.getMessage());
        }
    }
}