package com.app_language.hoctiengtrung_online.Ticket.service;



import com.app_language.hoctiengtrung_online.Ticket.dto.LocationDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Location;
import com.app_language.hoctiengtrung_online.Ticket.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public Location createLocation(LocationDTO dto) {
        Location location = new Location();
        location.setName(dto.getName());
        location.setAddress(dto.getAddress());
        location.setDescription(dto.getDescription());
        return locationRepository.save(location);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}