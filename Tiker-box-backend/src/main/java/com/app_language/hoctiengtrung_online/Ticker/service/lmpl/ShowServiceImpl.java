package com.app_language.hoctiengtrung_online.Ticker.service.lmpl;



import com.app_language.hoctiengtrung_online.Ticker.dto.ShowRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.ShowUpdateRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ArtistResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.CompanyResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.LocationResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ShowDetailResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.*;
import com.app_language.hoctiengtrung_online.Ticker.repository.ArtistRepository;
import com.app_language.hoctiengtrung_online.Ticker.repository.CompanyRepository;
import com.app_language.hoctiengtrung_online.Ticker.repository.LocationRepository;
import com.app_language.hoctiengtrung_online.Ticker.repository.ShowRepository;
import com.app_language.hoctiengtrung_online.Ticker.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShowServiceImpl implements ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public Show createShow(ShowRequestDTO showRequestDTO) {
        // Validate company exists
        Company company = companyRepository.findById(showRequestDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + showRequestDTO.getCompanyId()));

        // Validate location exists
        Location location = locationRepository.findById(showRequestDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + showRequestDTO.getLocationId()));

        // Validate artists exist
        if (showRequestDTO.getArtistIds() != null) {
            for (String artistId : showRequestDTO.getArtistIds()) {
                artistRepository.findById(artistId)
                        .orElseThrow(() -> new RuntimeException("Artist not found with id: " + artistId));
            }
        }

        Show show = new Show();
        show.setName(showRequestDTO.getName());
        show.setDescription(showRequestDTO.getDescription());
        show.setGenre(showRequestDTO.getGenre());
        show.setStartTime(showRequestDTO.getStartTime());
        show.setEndTime(showRequestDTO.getEndTime());
        show.setLocationId(showRequestDTO.getLocationId());
        show.setLocationAddress(location.getAddress()); // Set address from location
        show.setArtistIds(showRequestDTO.getArtistIds());
        show.setImageUrls(showRequestDTO.getImageUrls());
        show.setTicketTypes(showRequestDTO.getTicketTypes());
        show.setCompanyId(showRequestDTO.getCompanyId());
        show.setActive(true);
        show.setCreatedAt(LocalDateTime.now());
        show.setUpdatedAt(LocalDateTime.now());

        // Initialize ticket type quantities
        if (show.getTicketTypes() != null) {
            for (TicketType ticketType : show.getTicketTypes()) {
                if (ticketType.getAvailableQuantity() == null) {
                    ticketType.setAvailableQuantity(ticketType.getQuantity());
                }
            }
        }

        return showRepository.save(show);
    }

    @Override
    public Show updateShow(String id, ShowUpdateRequestDTO showUpdateRequestDTO) {
        Optional<Show> showOpt = showRepository.findById(id);
        if (showOpt.isPresent()) {
            Show show = showOpt.get();

            // Update location if provided
            if (showUpdateRequestDTO.getLocationId() != null) {
                Location location = locationRepository.findById(showUpdateRequestDTO.getLocationId())
                        .orElseThrow(() -> new RuntimeException("Location not found with id: " + showUpdateRequestDTO.getLocationId()));
                show.setLocationId(showUpdateRequestDTO.getLocationId());
                show.setLocationAddress(location.getAddress());
            }

            // Update artists if provided
            if (showUpdateRequestDTO.getArtistIds() != null) {
                for (String artistId : showUpdateRequestDTO.getArtistIds()) {
                    artistRepository.findById(artistId)
                            .orElseThrow(() -> new RuntimeException("Artist not found with id: " + artistId));
                }
                show.setArtistIds(showUpdateRequestDTO.getArtistIds());
            }

            show.setName(showUpdateRequestDTO.getName());
            show.setDescription(showUpdateRequestDTO.getDescription());
            show.setGenre(showUpdateRequestDTO.getGenre());
            show.setStartTime(showUpdateRequestDTO.getStartTime());
            show.setEndTime(showUpdateRequestDTO.getEndTime());

            if (showUpdateRequestDTO.getImageUrls() != null) {
                show.setImageUrls(showUpdateRequestDTO.getImageUrls());
            }

            if (showUpdateRequestDTO.getTicketTypes() != null) {
                show.setTicketTypes(showUpdateRequestDTO.getTicketTypes());
            }

            show.setUpdatedAt(LocalDateTime.now());

            return showRepository.save(show);
        }
        throw new RuntimeException("Show not found with id: " + id);
    }

    @Override
    public void deleteShow(String id) {
        Optional<Show> showOpt = showRepository.findById(id);
        if (showOpt.isPresent()) {
            Show show = showOpt.get();
            show.setActive(false);
            show.setUpdatedAt(LocalDateTime.now());
            showRepository.save(show);
        } else {
            throw new RuntimeException("Show not found with id: " + id);
        }
    }

    @Override
    public Show getShowById(String id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + id));
    }

    @Override
    public ShowDetailResponseDTO getShowDetailById(String id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + id));

        ShowDetailResponseDTO showDetail = new ShowDetailResponseDTO();
        showDetail.setId(show.getId());
        showDetail.setName(show.getName());
        showDetail.setDescription(show.getDescription());
        showDetail.setGenre(show.getGenre());
        showDetail.setStartTime(show.getStartTime());
        showDetail.setEndTime(show.getEndTime());
        showDetail.setImageUrls(show.getImageUrls());
        showDetail.setTicketTypes(show.getTicketTypes());
        showDetail.setActive(show.isActive());
        showDetail.setCreatedAt(show.getCreatedAt());
        showDetail.setUpdatedAt(show.getUpdatedAt());

        // Fetch and set company details
        Company company = companyRepository.findById(show.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + show.getCompanyId()));
        CompanyResponseDTO companyDTO = new CompanyResponseDTO();
        companyDTO.setId(company.getId());
        companyDTO.setCode(company.getCode());
        companyDTO.setName(company.getName());
        companyDTO.setDescription(company.getDescription());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setPhone(company.getPhone());
        companyDTO.setEmail(company.getEmail());
        companyDTO.setActive(company.isActive());
        companyDTO.setCreatedAt(company.getCreatedAt());
        companyDTO.setUpdatedAt(company.getUpdatedAt());
        showDetail.setCompany(companyDTO);

        // Fetch and set location details
        Location location = locationRepository.findById(show.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + show.getLocationId()));
        LocationResponseDTO locationDTO = new LocationResponseDTO();
        locationDTO.setId(location.getId());
        locationDTO.setName(location.getName());
        locationDTO.setAddress(location.getAddress());
        locationDTO.setDescription(location.getDescription());
        locationDTO.setCapacity(location.getCapacity());
        locationDTO.setActive(location.isActive());
        locationDTO.setCreatedAt(location.getCreatedAt());
        locationDTO.setUpdatedAt(location.getUpdatedAt());
        showDetail.setLocation(locationDTO);

        // Fetch and set artist details
        if (show.getArtistIds() != null && !show.getArtistIds().isEmpty()) {
            List<Artist> artists = artistRepository.findAllById(show.getArtistIds());
            List<ArtistResponseDTO> artistDTOs = artists.stream().map(artist -> {
                ArtistResponseDTO artistDTO = new ArtistResponseDTO();
                artistDTO.setId(artist.getId());
                artistDTO.setName(artist.getName());
                artistDTO.setDescription(artist.getDescription());
                artistDTO.setCompanyCode(artist.getCompanyCode());
                artistDTO.setImageUrl(artist.getImageUrl());
                artistDTO.setActive(artist.isActive());
                artistDTO.setCreatedAt(artist.getCreatedAt());
                artistDTO.setUpdatedAt(artist.getUpdatedAt());
                return artistDTO;
            }).collect(Collectors.toList());
            showDetail.setArtists(artistDTOs);
        }

        return showDetail;
    }

    @Override
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    @Override
    public List<Show> getShowsByCompany(String companyId) {
        return showRepository.findByCompanyId(companyId);
    }

    @Override
    public List<Show> getActiveShows() {
        return showRepository.findByActiveTrue();
    }

    @Override
    public List<Show> getShowsByArtist(String artistId) {
        return showRepository.findByArtistId(artistId);
    }

    @Override
    public List<Show> getShowsByLocation(String locationId) {
        return showRepository.findByLocationId(locationId);
    }

    @Override
    public List<Show> searchShows(String keyword) {
        return showRepository.searchActiveShows(keyword);
    }

    @Override
    public Show activateShow(String id) {
        Optional<Show> showOpt = showRepository.findById(id);
        if (showOpt.isPresent()) {
            Show show = showOpt.get();
            show.setActive(true);
            show.setUpdatedAt(LocalDateTime.now());
            return showRepository.save(show);
        }
        throw new RuntimeException("Show not found with id: " + id);
    }

    @Override
    public Show deactivateShow(String id) {
        Optional<Show> showOpt = showRepository.findById(id);
        if (showOpt.isPresent()) {
            Show show = showOpt.get();
            show.setActive(false);
            show.setUpdatedAt(LocalDateTime.now());
            return showRepository.save(show);
        }
        throw new RuntimeException("Show not found with id: " + id);
    }

    @Override
    public Show addImagesToShow(String showId, List<String> imageUrls) {
        Optional<Show> showOpt = showRepository.findById(showId);
        if (showOpt.isPresent()) {
            Show show = showOpt.get();
            if (show.getImageUrls() == null) {
                show.setImageUrls(imageUrls);
            } else {
                show.getImageUrls().addAll(imageUrls);
            }
            show.setUpdatedAt(LocalDateTime.now());
            return showRepository.save(show);
        }
        throw new RuntimeException("Show not found with id: " + showId);
    }

    @Override
    public List<Show> getUpcomingShows() {
        return showRepository.findUpcomingActiveShows(LocalDateTime.now());
    }

    @Override
    public List<Show> getShowsByGenre(String genre) {
        return showRepository.findActiveShowsByGenre(genre);
    }

    @Override
    public List<Show> getShowsWithAvailableTickets() {
        return showRepository.findActiveShowsWithAvailableTickets();
    }
}