package com.app_language.hoctiengtrung_online.Ticket.controller;

import com.app_language.hoctiengtrung_online.Ticket.dto.ShowDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Show;
import com.app_language.hoctiengtrung_online.Ticket.service.ShowService;
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

import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @Autowired
    private ObjectMapper objectMapper;

    @Operation(
            summary = "Create new show",
            description = "Create a new show with multiple images and ticket types"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createShow(
            @Parameter(
                    description = "Show data in JSON format",
                    schema = @Schema(implementation = ShowDTO.class),
                    example = "{\"name\":\"Show Name\",\"description\":\"Show description\",\"genre\":\"Pop\",\"startTime\":\"2024-12-25T19:00:00\",\"endTime\":\"2024-12-25T23:00:00\",\"locationId\":\"67a1b2c3d4e5f67890123456\",\"artistIds\":[\"67a1b2c3d4e5f67890123457\"],\"ticketTypes\":[{\"name\":\"VIP\",\"description\":\"VIP Ticket\",\"price\":500000,\"quantity\":100}],\"companyId\":\"67a1b2c3d4e5f67890123459\"}"
            )
            @RequestPart("show") String showString,

            @Parameter(description = "Show images (multiple files allowed)")
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        try {
            // Convert JSON string to ShowDTO
            ShowDTO showDTO = objectMapper.readValue(showString, ShowDTO.class);

            // Debug log
            System.out.println("=== DEBUG SHOW REQUEST ===");
            System.out.println("Show JSON: " + showString);
            System.out.println("Images count: " + (images != null ? images.size() : 0));
            if (images != null) {
                for (int i = 0; i < images.size(); i++) {
                    MultipartFile file = images.get(i);
                    System.out.println("Image " + i + ": " + file.getOriginalFilename() + " (" + file.getSize() + " bytes)");
                }
            }

            // Create show
            Show newShow = showService.createShow(showDTO, images);
            return ResponseEntity.ok(newShow);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating show: " + e.getMessage());
        }
    }

    @Operation(summary = "Get show by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getShow(@PathVariable String id) {
        try {
            Show show = showService.getShowById(id);
            if (show != null) {
                return ResponseEntity.ok(show);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Show not found with id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving show: " + e.getMessage());
        }
    }

    @Operation(summary = "Get all shows")
    @GetMapping
    public ResponseEntity<?> getAllShows() {
        try {
            List<Show> shows = showService.getAllShows();
            return ResponseEntity.ok(shows);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving shows: " + e.getMessage());
        }
    }

    @Operation(summary = "Get shows by company ID")
    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getShowsByCompany(@PathVariable String companyId) {
        try {
            List<Show> shows = showService.getShowsByCompanyId(companyId);
            return ResponseEntity.ok(shows);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving shows: " + e.getMessage());
        }
    }
}