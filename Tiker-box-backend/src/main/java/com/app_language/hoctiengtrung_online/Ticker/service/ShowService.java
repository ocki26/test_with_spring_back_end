package com.app_language.hoctiengtrung_online.Ticker.service;



import com.app_language.hoctiengtrung_online.Ticker.dto.ShowRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.ShowUpdateRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ShowDetailResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Show;
import java.util.List;

public interface ShowService {
    Show createShow(ShowRequestDTO showRequestDTO);
    Show updateShow(String id, ShowUpdateRequestDTO showUpdateRequestDTO);
    void deleteShow(String id);
    Show getShowById(String id);
    ShowDetailResponseDTO getShowDetailById(String id);
    List<Show> getAllShows();
    List<Show> getShowsByCompany(String companyId);
    List<Show> getActiveShows();
    List<Show> getShowsByArtist(String artistId);
    List<Show> getShowsByLocation(String locationId);
    List<Show> searchShows(String keyword);
    Show activateShow(String id);
    Show deactivateShow(String id);
    Show addImagesToShow(String showId, List<String> imageUrls);
    List<Show> getUpcomingShows();
    List<Show> getShowsByGenre(String genre);
    List<Show> getShowsWithAvailableTickets();
}