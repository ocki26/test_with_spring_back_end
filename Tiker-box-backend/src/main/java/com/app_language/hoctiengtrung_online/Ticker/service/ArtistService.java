package com.app_language.hoctiengtrung_online.Ticker.service;



import com.app_language.hoctiengtrung_online.Ticker.dto.ArtistRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Artist;
import java.util.List;

public interface ArtistService {
    Artist createArtist(ArtistRequestDTO artistRequestDTO);
    Artist updateArtist(String id, ArtistRequestDTO artistRequestDTO);
    void deleteArtist(String id);
    Artist getArtistById(String id);
    List<Artist> getAllArtists();
    List<Artist> getArtistsByCompany(String companyCode);
    List<Artist> getActiveArtists();
    Artist activateArtist(String id);
    Artist deactivateArtist(String id);
    List<Artist> searchArtists(String keyword);
    boolean existsByNameAndCompanyCode(String name, String companyCode);
}