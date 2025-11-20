package com.app_language.hoctiengtrung_online.Ticker.service.lmpl;


import com.app_language.hoctiengtrung_online.Ticker.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String storeFile(MultipartFile file) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }

    @Override
    public List<String> storeMultipleFiles(List<MultipartFile> files) {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    fileNames.add(storeFile(file));
                } catch (Exception e) {
                    // Log error but continue with other files
                    System.err.println("Failed to store file: " + file.getOriginalFilename() + ", error: " + e.getMessage());
                }
            }
        }
        return fileNames;
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }

    @Override
    public String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }
}