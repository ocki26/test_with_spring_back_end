package com.app_language.hoctiengtrung_online.Ticker.dto.Response;




public class TickerLoginResponseDTO {
    private String token;
    private TickerUserResponseDTO user;
    private String message;

    // Constructor mặc định
    public TickerLoginResponseDTO() {}

    // Constructor với tham số
    public TickerLoginResponseDTO(String token, TickerUserResponseDTO user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TickerUserResponseDTO getUser() {
        return user;
    }

    public void setUser(TickerUserResponseDTO user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TickerLoginResponseDTO{" +
                "token='" + (token != null ? "[PROTECTED]" : "null") + '\'' +
                ", user=" + user +
                ", message='" + message + '\'' +
                '}';
    }
}