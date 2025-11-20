package com.app_language.hoctiengtrung_online.Ticker.service;



import com.app_language.hoctiengtrung_online.Ticker.dto.LocationRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Location;
import java.util.List;

public interface LocationService {
    Location createLocation(LocationRequestDTO locationRequestDTO);
    Location updateLocation(String id, LocationRequestDTO locationRequestDTO);
    void deleteLocation(String id);
    Location getLocationById(String id);
    List<Location> getAllLocations();
    List<Location> getActiveLocations();
    List<Location> searchLocations(String keyword);
    Location activateLocation(String id);
    Location deactivateLocation(String id);
    List<Location> getLocationsWithMinCapacity(Integer minCapacity);
    List<Location> getLocationsWithMaxCapacity(Integer maxCapacity);
}