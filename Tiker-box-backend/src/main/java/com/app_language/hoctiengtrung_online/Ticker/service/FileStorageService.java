package com.app_language.hoctiengtrung_online.Ticker.service;



import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface FileStorageService {
    String storeFile(MultipartFile file);
    List<String> storeMultipleFiles(List<MultipartFile> files);
    void deleteFile(String fileName);
    String generateFileName(String originalFileName);
}