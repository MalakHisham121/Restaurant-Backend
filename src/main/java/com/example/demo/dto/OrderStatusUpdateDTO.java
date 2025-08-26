package com.example.demo.dto;

public class OrderStatusUpdateDTO {
    private Long orderId;
    private String status;

    // Default constructor
    public OrderStatusUpdateDTO() {}

    // Constructor with parameters
    public OrderStatusUpdateDTO(Long orderId, String status) {
        this.orderId = orderId;
        this.status = status;
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
}
