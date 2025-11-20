package com.app_language.hoctiengtrung_online.Ticker.controller;


import com.app_language.hoctiengtrung_online.Ticker.dto.LocationRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ApiResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Location;
import com.app_language.hoctiengtrung_online.Ticker.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@Tag(name = "Location Management", description = "APIs for managing locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    @Operation(summary = "Create a new location")
    public ResponseEntity<ApiResponseDTO> createLocation(@Valid @RequestBody LocationRequestDTO locationRequestDTO) {
        Location location = locationService.createLocation(locationRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Location created successfully", location));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a location")
    public ResponseEntity<ApiResponseDTO> updateLocation(@PathVariable String id,
                                                         @Valid @RequestBody LocationRequestDTO locationRequestDTO) {
        Location location = locationService.updateLocation(id, locationRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Location updated successfully", location));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a location")
    public ResponseEntity<ApiResponseDTO> deleteLocation(@PathVariable String id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Location deleted successfully", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get location by ID")
    public ResponseEntity<ApiResponseDTO> getLocationById(@PathVariable String id) {
        Location location = locationService.getLocationById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Location retrieved successfully", location));
    }

    @GetMapping
    @Operation(summary = "Get all locations")
    public ResponseEntity<ApiResponseDTO> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(ApiResponseDTO.success("Locations retrieved successfully", locations));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active locations")
    public ResponseEntity<ApiResponseDTO> getActiveLocations() {
        List<Location> locations = locationService.getActiveLocations();
        return ResponseEntity.ok(ApiResponseDTO.success("Active locations retrieved successfully", locations));
    }

    @GetMapping("/search")
    @Operation(summary = "Search locations by name or address")
    public ResponseEntity<ApiResponseDTO> searchLocations(@RequestParam String keyword) {
        List<Location> locations = locationService.searchLocations(keyword);
        return ResponseEntity.ok(ApiResponseDTO.success("Locations retrieved successfully", locations));
    }
}