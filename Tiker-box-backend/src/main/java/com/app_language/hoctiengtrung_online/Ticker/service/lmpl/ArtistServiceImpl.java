package com.app_language.hoctiengtrung_online.Ticker.service.lmpl;



import com.app_language.hoctiengtrung_online.Ticker.dto.ArtistRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Artist;
import com.app_language.hoctiengtrung_online.Ticker.repository.ArtistRepository;
import com.app_language.hoctiengtrung_online.Ticker.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public Artist createArtist(ArtistRequestDTO artistRequestDTO) {
        // Check if artist with same name and company code already exists
        if (existsByNameAndCompanyCode(artistRequestDTO.getName(), artistRequestDTO.getCompanyCode())) {
            throw new RuntimeException("Artist with name '" + artistRequestDTO.getName() + "' already exists in company");
        }

        Artist artist = new Artist();
        artist.setName(artistRequestDTO.getName());
        artist.setDescription(artistRequestDTO.getDescription());
        artist.setCompanyCode(artistRequestDTO.getCompanyCode());
        artist.setImageUrl(artistRequestDTO.getImageUrl());
        artist.setActive(true);
        artist.setCreatedAt(LocalDateTime.now());
        artist.setUpdatedAt(LocalDateTime.now());

        return artistRepository.save(artist);
    }

    @Override
    public Artist updateArtist(String id, ArtistRequestDTO artistRequestDTO) {
        Optional<Artist> artistOpt = artistRepository.findById(id);
        if (artistOpt.isPresent()) {
            Artist artist = artistOpt.get();

            // Check if name is being changed and conflicts with existing artist
            if (!artist.getName().equals(artistRequestDTO.getName()) &&
                    existsByNameAndCompanyCode(artistRequestDTO.getName(), artist.getCompanyCode())) {
                throw new RuntimeException("Artist with name '" + artistRequestDTO.getName() + "' already exists in company");
            }

            artist.setName(artistRequestDTO.getName());
            artist.setDescription(artistRequestDTO.getDescription());
            artist.setImageUrl(artistRequestDTO.getImageUrl());
            artist.setUpdatedAt(LocalDateTime.now());

            return artistRepository.save(artist);
        }
        throw new RuntimeException("Artist not found with id: " + id);
    }

    @Override
    public void deleteArtist(String id) {
        Optional<Artist> artistOpt = artistRepository.findById(id);
        if (artistOpt.isPresent()) {
            Artist artist = artistOpt.get();
            artist.setActive(false);
            artist.setUpdatedAt(LocalDateTime.now());
            artistRepository.save(artist);
        } else {
            throw new RuntimeException("Artist not found with id: " + id);
        }
    }

    @Override
    public Artist getArtistById(String id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + id));
    }

    @Override
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    @Override
    public List<Artist> getArtistsByCompany(String companyCode) {
        return artistRepository.findByCompanyCode(companyCode);
    }

    @Override
    public List<Artist> getActiveArtists() {
        return artistRepository.findByActiveTrue();
    }

    @Override
    public Artist activateArtist(String id) {
        Optional<Artist> artistOpt = artistRepository.findById(id);
        if (artistOpt.isPresent()) {
            Artist artist = artistOpt.get();
            artist.setActive(true);
            artist.setUpdatedAt(LocalDateTime.now());
            return artistRepository.save(artist);
        }
        throw new RuntimeException("Artist not found with id: " + id);
    }

    @Override
    public Artist deactivateArtist(String id) {
        Optional<Artist> artistOpt = artistRepository.findById(id);
        if (artistOpt.isPresent()) {
            Artist artist = artistOpt.get();
            artist.setActive(false);
            artist.setUpdatedAt(LocalDateTime.now());
            return artistRepository.save(artist);
        }
        throw new RuntimeException("Artist not found with id: " + id);
    }

    @Override
    public List<Artist> searchArtists(String keyword) {
        return artistRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public boolean existsByNameAndCompanyCode(String name, String companyCode) {
        return artistRepository.existsByNameAndCompanyCode(name, companyCode);
    }
}