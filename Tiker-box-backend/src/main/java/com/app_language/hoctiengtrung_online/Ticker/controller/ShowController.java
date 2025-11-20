package com.app_language.hoctiengtrung_online.Ticker.controller;



import com.app_language.hoctiengtrung_online.Ticker.dto.ShowRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.ShowUpdateRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ApiResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ShowDetailResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ShowResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.Show;
import com.app_language.hoctiengtrung_online.Ticker.service.ShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
@Tag(name = "Show Management", description = "APIs for managing shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping
    @Operation(summary = "Create a new show")
    public ResponseEntity<ApiResponseDTO> createShow(@Valid @RequestBody ShowRequestDTO showRequestDTO) {
        Show show = showService.createShow(showRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Show created successfully", show));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a show")
    public ResponseEntity<ApiResponseDTO> updateShow(@PathVariable String id,
                                                     @Valid @RequestBody ShowUpdateRequestDTO showUpdateRequestDTO) {
        Show show = showService.updateShow(id, showUpdateRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Show updated successfully", show));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a show")
    public ResponseEntity<ApiResponseDTO> deleteShow(@PathVariable String id) {
        showService.deleteShow(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Show deleted successfully", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get show by ID")
    public ResponseEntity<ApiResponseDTO> getShowById(@PathVariable String id) {
        Show show = showService.getShowById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Show retrieved successfully", show));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Get show details with full information")
    public ResponseEntity<ApiResponseDTO> getShowDetailById(@PathVariable String id) {
        ShowDetailResponseDTO showDetail = showService.getShowDetailById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Show details retrieved successfully", showDetail));
    }

    @GetMapping
    @Operation(summary = "Get all shows")
    public ResponseEntity<ApiResponseDTO> getAllShows() {
        List<Show> shows = showService.getAllShows();
        return ResponseEntity.ok(ApiResponseDTO.success("Shows retrieved successfully", shows));
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get shows by company")
    public ResponseEntity<ApiResponseDTO> getShowsByCompany(@PathVariable String companyId) {
        List<Show> shows = showService.getShowsByCompany(companyId);
        return ResponseEntity.ok(ApiResponseDTO.success("Shows retrieved successfully", shows));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active shows")
    public ResponseEntity<ApiResponseDTO> getActiveShows() {
        List<Show> shows = showService.getActiveShows();
        return ResponseEntity.ok(ApiResponseDTO.success("Active shows retrieved successfully", shows));
    }

    @GetMapping("/artist/{artistId}")
    @Operation(summary = "Get shows by artist")
    public ResponseEntity<ApiResponseDTO> getShowsByArtist(@PathVariable String artistId) {
        List<Show> shows = showService.getShowsByArtist(artistId);
        return ResponseEntity.ok(ApiResponseDTO.success("Shows retrieved successfully", shows));
    }

    @GetMapping("/location/{locationId}")
    @Operation(summary = "Get shows by location")
    public ResponseEntity<ApiResponseDTO> getShowsByLocation(@PathVariable String locationId) {
        List<Show> shows = showService.getShowsByLocation(locationId);
        return ResponseEntity.ok(ApiResponseDTO.success("Shows retrieved successfully", shows));
    }

    @GetMapping("/search")
    @Operation(summary = "Search shows by name or genre")
    public ResponseEntity<ApiResponseDTO> searchShows(@RequestParam String keyword) {
        List<Show> shows = showService.searchShows(keyword);
        return ResponseEntity.ok(ApiResponseDTO.success("Shows retrieved successfully", shows));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a show")
    public ResponseEntity<ApiResponseDTO> activateShow(@PathVariable String id) {
        Show show = showService.activateShow(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Show activated successfully", show));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a show")
    public ResponseEntity<ApiResponseDTO> deactivateShow(@PathVariable String id) {
        Show show = showService.deactivateShow(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Show deactivated successfully", show));
    }
}