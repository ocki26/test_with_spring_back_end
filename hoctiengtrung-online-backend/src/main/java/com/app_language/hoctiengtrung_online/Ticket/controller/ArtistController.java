package com.app_language.hoctiengtrung_online.Ticket.controller;

import com.app_language.hoctiengtrung_online.Ticket.dto.ArtistDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Artist;
import com.app_language.hoctiengtrung_online.Ticket.service.ArtistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private ObjectMapper objectMapper;

    @Operation(summary = "Create new artist", description = "Create a new artist with image upload")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createArtist(
            @Parameter(description = "Artist data in JSON format",
                    schema = @Schema(implementation = ArtistDTO.class),
                    example = "{\"name\":\"Artist Name\",\"description\":\"Artist description\",\"companyCode\":\"COMP001\"}")
            @RequestPart("artist") String artistString,

            @Parameter(description = "Artist image file")
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            // Convert JSON string to ArtistDTO
            ArtistDTO artistDTO = objectMapper.readValue(artistString, ArtistDTO.class);

            System.out.println("=== DEBUG MULTIPART REQUEST ===");
            System.out.println("Artist JSON: " + artistString);
            System.out.println("Image: " + (image != null ? image.getOriginalFilename() : "null"));

            Artist newArtist = artistService.createArtist(artistDTO, image);
            return ResponseEntity.ok(newArtist);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error processing request: " + e.getMessage());
        }
    }

    // Thêm các endpoint khác để Swagger hiển thị đầy đủ
    @Operation(summary = "Get all artists")
    @GetMapping
    public ResponseEntity<?> getAllArtists() {
        try {
            return ResponseEntity.ok(artistService.getAllArtists());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving artists: " + e.getMessage());
        }
    }

    @Operation(summary = "Get artist by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getArtistById(@PathVariable String id) {
        try {
            return artistService.getArtistById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving artist: " + e.getMessage());
        }
    }
}