package com.example.demo.dto;

public class NotificationResponseDTO {
    private String message;

    public NotificationResponseDTO() {}

    public NotificationResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
