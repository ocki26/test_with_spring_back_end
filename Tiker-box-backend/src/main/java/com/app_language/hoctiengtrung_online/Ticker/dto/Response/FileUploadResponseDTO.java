package com.app_language.hoctiengtrung_online.Ticker.dto.Response;


import java.util.List;

public class FileUploadResponseDTO {
    private boolean success;
    private String message;
    private List<String> fileUrls;
    private int uploadedCount;

    public FileUploadResponseDTO() {}

    public FileUploadResponseDTO(boolean success, String message, List<String> fileUrls, int uploadedCount) {
        this.success = success;
        this.message = message;
        this.fileUrls = fileUrls;
        this.uploadedCount = uploadedCount;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<String> getFileUrls() { return fileUrls; }
    public void setFileUrls(List<String> fileUrls) { this.fileUrls = fileUrls; }
    public int getUploadedCount() { return uploadedCount; }
    public void setUploadedCount(int uploadedCount) { this.uploadedCount = uploadedCount; }
}