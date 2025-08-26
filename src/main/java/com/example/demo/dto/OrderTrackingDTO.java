package com.example.demo.dto;

public class OrderTrackingDTO {
    private Long orderId;
    private String status;
    private String estimatedTime;

    public OrderTrackingDTO() {}

    public OrderTrackingDTO(Long orderId, String status, String estimatedTime) {
        this.orderId = orderId;
        this.status = status;
        this.estimatedTime = estimatedTime;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
