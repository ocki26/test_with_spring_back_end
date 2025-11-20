package com.app_language.hoctiengtrung_online.Ticker.controller;



import com.app_language.hoctiengtrung_online.Ticker.dto.ArtistRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ApiResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ArtistResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Artist;
import com.app_language.hoctiengtrung_online.Ticker.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/artists")
@Tag(name = "Artist Management", description = "APIs for managing artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @PostMapping
    @Operation(summary = "Create a new artist")
    public ResponseEntity<ApiResponseDTO> createArtist(@Valid @RequestBody ArtistRequestDTO artistRequestDTO) {
        Artist artist = artistService.createArtist(artistRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Artist created successfully", artist));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an artist")
    public ResponseEntity<ApiResponseDTO> updateArtist(@PathVariable String id,
                                                       @Valid @RequestBody ArtistRequestDTO artistRequestDTO) {
        Artist artist = artistService.updateArtist(id, artistRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Artist updated successfully", artist));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an artist")
    public ResponseEntity<ApiResponseDTO> deleteArtist(@PathVariable String id) {
        artistService.deleteArtist(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Artist deleted successfully", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get artist by ID")
    public ResponseEntity<ApiResponseDTO> getArtistById(@PathVariable String id) {
        Artist artist = artistService.getArtistById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Artist retrieved successfully", artist));
    }

    @GetMapping
    @Operation(summary = "Get all artists")
    public ResponseEntity<ApiResponseDTO> getAllArtists() {
        List<Artist> artists = artistService.getAllArtists();
        return ResponseEntity.ok(ApiResponseDTO.success("Artists retrieved successfully", artists));
    }

    @GetMapping("/company/{companyCode}")
    @Operation(summary = "Get artists by company code")
    public ResponseEntity<ApiResponseDTO> getArtistsByCompany(@PathVariable String companyCode) {
        List<Artist> artists = artistService.getArtistsByCompany(companyCode);
        return ResponseEntity.ok(ApiResponseDTO.success("Artists retrieved successfully", artists));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active artists")
    public ResponseEntity<ApiResponseDTO> getActiveArtists() {
        List<Artist> artists = artistService.getActiveArtists();
        return ResponseEntity.ok(ApiResponseDTO.success("Active artists retrieved successfully", artists));
    }
}