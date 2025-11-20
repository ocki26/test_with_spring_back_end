package com.app_language.hoctiengtrung_online.Ticker.service.lmpl;



import com.app_language.hoctiengtrung_online.Ticker.dto.LocationRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Location;
import com.app_language.hoctiengtrung_online.Ticker.repository.LocationRepository;
import com.app_language.hoctiengtrung_online.Ticker.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Location createLocation(LocationRequestDTO locationRequestDTO) {
        // Check if location with same name and address already exists
        if (locationRepository.existsByNameAndAddress(locationRequestDTO.getName(), locationRequestDTO.getAddress())) {
            throw new RuntimeException("Location with name '" + locationRequestDTO.getName() + "' and address already exists");
        }

        Location location = new Location();
        location.setName(locationRequestDTO.getName());
        location.setAddress(locationRequestDTO.getAddress());
        location.setDescription(locationRequestDTO.getDescription());
        location.setCapacity(locationRequestDTO.getCapacity());
        location.setActive(true);
        location.setCreatedAt(LocalDateTime.now());
        location.setUpdatedAt(LocalDateTime.now());

        return locationRepository.save(location);
    }

    @Override
    public Location updateLocation(String id, LocationRequestDTO locationRequestDTO) {
        Optional<Location> locationOpt = locationRepository.findById(id);
        if (locationOpt.isPresent()) {
            Location location = locationOpt.get();

            // Check if name and address combination is being changed and conflicts
            if ((!location.getName().equals(locationRequestDTO.getName()) ||
                    !location.getAddress().equals(locationRequestDTO.getAddress())) &&
                    locationRepository.existsByNameAndAddress(locationRequestDTO.getName(), locationRequestDTO.getAddress())) {
                throw new RuntimeException("Location with name and address combination already exists");
            }

            location.setName(locationRequestDTO.getName());
            location.setAddress(locationRequestDTO.getAddress());
            location.setDescription(locationRequestDTO.getDescription());
            location.setCapacity(locationRequestDTO.getCapacity());
            location.setUpdatedAt(LocalDateTime.now());

            return locationRepository.save(location);
        }
        throw new RuntimeException("Location not found with id: " + id);
    }

    @Override
    public void deleteLocation(String id) {
        Optional<Location> locationOpt = locationRepository.findById(id);
        if (locationOpt.isPresent()) {
            Location location = locationOpt.get();
            location.setActive(false);
            location.setUpdatedAt(LocalDateTime.now());
            locationRepository.save(location);
        } else {
            throw new RuntimeException("Location not found with id: " + id);
        }
    }

    @Override
    public Location getLocationById(String id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public List<Location> getActiveLocations() {
        return locationRepository.findByActiveTrue();
    }

    @Override
    public List<Location> searchLocations(String keyword) {
        return locationRepository.searchActiveLocations(keyword);
    }

    @Override
    public Location activateLocation(String id) {
        Optional<Location> locationOpt = locationRepository.findById(id);
        if (locationOpt.isPresent()) {
            Location location = locationOpt.get();
            location.setActive(true);
            location.setUpdatedAt(LocalDateTime.now());
            return locationRepository.save(location);
        }
        throw new RuntimeException("Location not found with id: " + id);
    }

    @Override
    public Location deactivateLocation(String id) {
        Optional<Location> locationOpt = locationRepository.findById(id);
        if (locationOpt.isPresent()) {
            Location location = locationOpt.get();
            location.setActive(false);
            location.setUpdatedAt(LocalDateTime.now());
            return locationRepository.save(location);
        }
        throw new RuntimeException("Location not found with id: " + id);
    }

    @Override
    public List<Location> getLocationsWithMinCapacity(Integer minCapacity) {
        return locationRepository.findActiveLocationsWithMinCapacity(minCapacity);
    }

    @Override
    public List<Location> getLocationsWithMaxCapacity(Integer maxCapacity) {
        return locationRepository.findActiveLocationsWithMaxCapacity(maxCapacity);
    }
}