package com.app_language.hoctiengtrung_online.Ticker.repository;



import com.app_language.hoctiengtrung_online.Ticker.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findByShowId(String showId);

    List<Ticket> findByBuyerEmail(String buyerEmail);

    Optional<Ticket> findByTicketCode(String ticketCode);

    List<Ticket> findByTicketTypeId(String ticketTypeId);

    List<Ticket> findByStatus(String status);

    @Query("{ 'showId': ?0, 'status': ?1 }")
    List<Ticket> findByShowIdAndStatus(String showId, String status);

    @Query("{ 'buyerEmail': ?0, 'status': ?1 }")
    List<Ticket> findByBuyerEmailAndStatus(String buyerEmail, String status);

    @Query("{ 'ticketTypeId': ?0, 'status': ?1 }")
    List<Ticket> findByTicketTypeIdAndStatus(String ticketTypeId, String status);

    @Query("{ 'showId': ?0, 'ticketTypeId': ?1 }")
    List<Ticket> findByShowIdAndTicketTypeId(String showId, String ticketTypeId);

    @Query("{ 'showId': ?0, 'ticketTypeId': ?1, 'status': ?2 }")
    List<Ticket> findByShowIdAndTicketTypeIdAndStatus(String showId, String ticketTypeId, String status);

    @Query(value = "{ 'showId': ?0 }", count = true)
    long countByShowId(String showId);

    @Query(value = "{ 'showId': ?0, 'status': ?1 }", count = true)
    long countByShowIdAndStatus(String showId, String status);

    @Query(value = "{ 'ticketTypeId': ?0 }", count = true)
    long countByTicketTypeId(String ticketTypeId);

    @Query(value = "{ 'ticketTypeId': ?0, 'status': ?1 }", count = true)
    long countByTicketTypeIdAndStatus(String ticketTypeId, String status);

    @Query("{ 'scannedBy': { $exists: true, $ne: null } }")
    List<Ticket> findScannedTickets();

    @Query("{ 'scannedBy': { $exists: true, $ne: null }, 'showId': ?0 }")
    List<Ticket> findScannedTicketsByShow(String showId);

    @Query("{ 'status': 'CONFIRMED', 'showId': ?0 }")
    List<Ticket> findConfirmedTicketsByShow(String showId);

    @Query("{ 'status': 'CANCELLED', 'showId': ?0 }")
    List<Ticket> findCancelledTicketsByShow(String showId);

    @Query(value = "{}", fields = "{ 'ticketCode': 1, 'showId': 1, 'status': 1 }")
    List<Ticket> findAllTicketCodes();

    boolean existsByTicketCode(String ticketCode);

    @Query("{ 'buyerEmail': ?0, 'showId': ?1 }")
    List<Ticket> findByBuyerEmailAndShowId(String buyerEmail, String showId);
}