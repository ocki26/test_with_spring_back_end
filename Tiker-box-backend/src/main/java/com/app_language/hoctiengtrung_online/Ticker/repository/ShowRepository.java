package com.app_language.hoctiengtrung_online.Ticker.repository;



import com.app_language.hoctiengtrung_online.Ticker.model.Show;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends MongoRepository<Show, String> {

    List<Show> findByCompanyId(String companyId);

    List<Show> findByActiveTrue();

    List<Show> findByLocationId(String locationId);

    @Query("{ 'artistIds': { $in: [?0] } }")
    List<Show> findByArtistId(String artistId);

    @Query("{ 'active': true, 'companyId': ?0 }")
    List<Show> findActiveShowsByCompany(String companyId);

    @Query("{ 'active': true, 'locationId': ?0 }")
    List<Show> findActiveShowsByLocation(String locationId);

    @Query("{ 'active': true, 'artistIds': { $in: [?0] } }")
    List<Show> findActiveShowsByArtist(String artistId);

    @Query("{ 'active': true, 'startTime': { $gte: ?0 }, 'endTime': { $lte: ?1 } }")
    List<Show> findActiveShowsBetweenDates(LocalDateTime start, LocalDateTime end);

    @Query("{ 'active': true, 'startTime': { $gte: ?0 } }")
    List<Show> findUpcomingActiveShows(LocalDateTime now);

    @Query("{ 'active': true, 'endTime': { $lt: ?0 } }")
    List<Show> findPastActiveShows(LocalDateTime now);

    @Query("{ 'active': true, $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } }, { 'genre': { $regex: ?0, $options: 'i' } } ] }")
    List<Show> searchActiveShows(String keyword);

    @Query("{ 'active': true, 'genre': { $regex: ?0, $options: 'i' } }")
    List<Show> findActiveShowsByGenre(String genre);

    @Query("{ 'active': true, 'companyId': ?0, 'startTime': { $gte: ?1 } }")
    List<Show> findUpcomingActiveShowsByCompany(String companyId, LocalDateTime now);

    @Query(value = "{ 'companyId': ?0 }", count = true)
    long countByCompanyId(String companyId);

    @Query(value = "{ 'active': true, 'companyId': ?0 }", count = true)
    long countActiveShowsByCompany(String companyId);

    @Query("{ 'active': true, 'ticketTypes.availableQuantity': { $gt: 0 } }")
    List<Show> findActiveShowsWithAvailableTickets();

    @Query("{ 'active': true, 'companyId': ?0, 'ticketTypes.availableQuantity': { $gt: 0 } }")
    List<Show> findActiveShowsWithAvailableTicketsByCompany(String companyId);
}