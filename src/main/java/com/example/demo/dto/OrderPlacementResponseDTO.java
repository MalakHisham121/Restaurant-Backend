package com.example.demo.dto;

public class OrderPlacementResponseDTO {
    private Long orderId;
    private String estimatedPreparationTime;
    private String message;

    public OrderPlacementResponseDTO() {}

    public OrderPlacementResponseDTO(Long orderId, String estimatedPreparationTime, String message) {
        this.orderId = orderId;
        this.estimatedPreparationTime = estimatedPreparationTime;
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getEstimatedPreparationTime() {
        return estimatedPreparationTime;
    }

    public void setEstimatedPreparationTime(String estimatedPreparationTime) {
        this.estimatedPreparationTime = estimatedPreparationTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
