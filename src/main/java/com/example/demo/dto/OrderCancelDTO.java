package com.example.demo.dto;

public class OrderCancelDTO {
    private Long orderId;

    public OrderCancelDTO() {}

    public OrderCancelDTO(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
