package com.app_language.hoctiengtrung_online.Ticket.controller;



import com.app_language.hoctiengtrung_online.Ticket.dto.ShowDTO;
import com.app_language.hoctiengtrung_online.Ticket.model.Show;
import com.app_language.hoctiengtrung_online.Ticket.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    // Tạo Show + Upload nhiều ảnh + Ticket Types
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Show> createShow(
            @RequestPart("show") ShowDTO showDTO, // JSON chứa thông tin show và các loại vé
            @RequestPart(value = "images", required = false) List<MultipartFile> images // Danh sách ảnh
    ) {
        Show newShow = showService.createShow(showDTO, images);
        return ResponseEntity.ok(newShow);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Show> getShow(@PathVariable String id) {
        return ResponseEntity.ok(showService.getShowById(id));
    }

    // API lấy danh sách show (có thể thêm phân trang sau này)
    @GetMapping
    public ResponseEntity<List<Show>> getAllShows() {
        return ResponseEntity.ok(showService.showRepository.findAll()); // Gọi tạm repo, nên chuyển qua service
    }
}