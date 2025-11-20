package com.app_language.hoctiengtrung_online.Ticker.controller;



import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ApiResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.FileUploadResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.service.FileStorageService;
import com.app_language.hoctiengtrung_online.Ticker.service.ShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/upload")
@Tag(name = "File Upload", description = "APIs for file upload")
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ShowService showService;

    @PostMapping("/single")
    @Operation(summary = "Upload a single file")
    public ResponseEntity<ApiResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        return ResponseEntity.ok(ApiResponseDTO.success("File uploaded successfully", fileName));
    }

    @PostMapping("/multiple")
    @Operation(summary = "Upload multiple files")
    public ResponseEntity<ApiResponseDTO> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) {
        List<String> fileNames = fileStorageService.storeMultipleFiles(files);
        FileUploadResponseDTO response = new FileUploadResponseDTO(
                true,
                "Files uploaded successfully",
                fileNames,
                fileNames.size()
        );
        return ResponseEntity.ok(ApiResponseDTO.success("Files uploaded successfully", response));
    }

    @PostMapping("/show/{showId}/images")
    @Operation(summary = "Upload images for a show")
    public ResponseEntity<ApiResponseDTO> uploadShowImages(@PathVariable String showId,
                                                           @RequestParam("files") List<MultipartFile> files) {
        List<String> fileNames = fileStorageService.storeMultipleFiles(files);
        showService.addImagesToShow(showId, fileNames);
        FileUploadResponseDTO response = new FileUploadResponseDTO(
                true,
                "Show images uploaded successfully",
                fileNames,
                fileNames.size()
        );
        return ResponseEntity.ok(ApiResponseDTO.success("Show images uploaded successfully", response));
    }

    @DeleteMapping("/file")
    @Operation(summary = "Delete a file")
    public ResponseEntity<ApiResponseDTO> deleteFile(@RequestParam String fileName) {
        fileStorageService.deleteFile(fileName);
        return ResponseEntity.ok(ApiResponseDTO.success("File deleted successfully", null));
    }
}