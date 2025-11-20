package com.app_language.hoctiengtrung_online.Ticker.repository;



import com.app_language.hoctiengtrung_online.Ticker.model.Show;
import java.util.List;

public interface CustomShowRepository {

    List<Show> findShowsWithTicketAvailability();

    List<Show> findShowsByMultipleArtists(List<String> artistIds);

    List<Show> findShowsWithTicketPriceRange(Double minPrice, Double maxPrice);

    List<Show> findPopularShows(int limit);
}