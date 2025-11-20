package com.app_language.hoctiengtrung_online.Ticket.controller;


import com.app_language.hoctiengtrung_online.Ticket.dto.LocationDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Location;
import com.app_language.hoctiengtrung_online.Ticket.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody LocationDTO locationDTO) {
        return ResponseEntity.ok(locationService.createLocation(locationDTO));
    }

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }
}