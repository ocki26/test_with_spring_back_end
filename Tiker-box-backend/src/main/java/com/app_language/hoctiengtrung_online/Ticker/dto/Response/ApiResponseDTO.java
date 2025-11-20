package com.app_language.hoctiengtrung_online.Ticker.dto.Response;



import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO {
    private boolean success;
    private String message;
    private Object data;
    private LocalDateTime timestamp;
    private String path;
    private Integer statusCode;

    public ApiResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponseDTO(boolean success, String message, Object data) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponseDTO(boolean success, String message, Object data, Integer statusCode) {
        this(success, message, data);
        this.statusCode = statusCode;
    }

    public ApiResponseDTO(boolean success, String message, Object data, Integer statusCode, String path) {
        this(success, message, data, statusCode);
        this.path = path;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    // Static factory methods for success responses
    public static ApiResponseDTO success(String message) {
        return new ApiResponseDTO(true, message, null);
    }

    public static ApiResponseDTO success(String message, Object data) {
        return new ApiResponseDTO(true, message, data);
    }

    public static ApiResponseDTO success(String message, Object data, Integer statusCode) {
        return new ApiResponseDTO(true, message, data, statusCode);
    }

    // Static factory methods for error responses
    public static ApiResponseDTO error(String message) {
        return new ApiResponseDTO(false, message, null);
    }

    public static ApiResponseDTO error(String message, Integer statusCode) {
        return new ApiResponseDTO(false, message, null, statusCode);
    }

    public static ApiResponseDTO error(String message, Object data) {
        return new ApiResponseDTO(false, message, data);
    }

    public static ApiResponseDTO error(String message, Object data, Integer statusCode) {
        return new ApiResponseDTO(false, message, data, statusCode);
    }

    // Builder pattern methods for fluent API
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String message;
        private Object data;
        private Integer statusCode;
        private String path;

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public Builder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public ApiResponseDTO build() {
            ApiResponseDTO response = new ApiResponseDTO();
            response.setSuccess(this.success);
            response.setMessage(this.message);
            response.setData(this.data);
            response.setStatusCode(this.statusCode);
            response.setPath(this.path);
            return response;
        }
    }

    @Override
    public String toString() {
        return "ApiResponseDTO{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                ", path='" + path + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}