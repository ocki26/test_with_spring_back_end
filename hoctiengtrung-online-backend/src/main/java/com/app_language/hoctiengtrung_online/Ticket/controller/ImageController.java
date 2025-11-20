package com.app_language.hoctiengtrung_online.Ticket.controller;




import com.app_language.hoctiengtrung_online.Ticket.model.Artist;
import com.app_language.hoctiengtrung_online.Ticket.model.Show;
import com.app_language.hoctiengtrung_online.Ticket.service.ArtistService;
import com.app_language.hoctiengtrung_online.Ticket.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ShowService showService;

    // API lấy ảnh Artist
    @GetMapping("/artists/{artistId}")
    public ResponseEntity<byte[]> getArtistImage(@PathVariable String artistId) {
        try {
            Optional<Artist> artist = artistService.getArtistById(artistId);
            if (artist.isPresent() && artist.get().getImageData() != null) {
                Artist artistObj = artist.get();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(artistObj.getImageContentType()));
                headers.setContentDispositionFormData("inline", artistObj.getImageFileName());
                headers.setCacheControl("max-age=3600"); // Cache 1 giờ

                return new ResponseEntity<>(artistObj.getImageData(), headers, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // API lấy ảnh Show (theo index)
    @GetMapping("/shows/{showId}/{imageIndex}")
    public ResponseEntity<byte[]> getShowImage(
            @PathVariable String showId,
            @PathVariable int imageIndex) {
        try {
            Show show = showService.getShowById(showId);
            if (show != null && show.getImages() != null &&
                    imageIndex >= 0 && imageIndex < show.getImages().size()) {

                Show.ShowImage image = show.getImages().get(imageIndex);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(image.getImageContentType()));
                headers.setContentDispositionFormData("inline", image.getImageFileName());
                headers.setCacheControl("max-age=3600");

                return new ResponseEntity<>(image.getImageData(), headers, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // API lấy ảnh đầu tiên của Show (thường dùng cho thumbnail)
    @GetMapping("/shows/{showId}/thumbnail")
    public ResponseEntity<byte[]> getShowThumbnail(@PathVariable String showId) {
        return getShowImage(showId, 0);
    }
}