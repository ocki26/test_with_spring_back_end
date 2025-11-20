package com.app_language.hoctiengtrung_online.Ticker.dto;


import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class FileUploadRequestDTO {
    private List<MultipartFile> files;
    private String showId;

    // Getters and Setters
    public List<MultipartFile> getFiles() { return files; }
    public void setFiles(List<MultipartFile> files) { this.files = files; }
    public String getShowId() { return showId; }
    public void setShowId(String showId) { this.showId = showId; }
}