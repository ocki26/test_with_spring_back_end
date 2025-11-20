package com.app_language.hoctiengtrung_online.Ticker.repository;



import com.app_language.hoctiengtrung_online.Ticker.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {

    List<Location> findByActiveTrue();

    List<Location> findByNameContainingIgnoreCase(String name);

    List<Location> findByAddressContainingIgnoreCase(String address);

    @Query("{ 'active': true, 'capacity': { $gte: ?0 } }")
    List<Location> findActiveLocationsWithMinCapacity(Integer minCapacity);

    @Query("{ 'active': true, 'capacity': { $lte: ?0 } }")
    List<Location> findActiveLocationsWithMaxCapacity(Integer maxCapacity);

    @Query("{ 'active': true, $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'address': { $regex: ?0, $options: 'i' } } ] }")
    List<Location> searchActiveLocations(String keyword);

    Optional<Location> findByNameAndAddress(String name, String address);

    boolean existsByNameAndAddress(String name, String address);

    @Query("{ 'active': true, 'name': ?0 }")
    List<Location> findActiveLocationsByName(String name);

    long countByActiveTrue();
}