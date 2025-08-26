package com.example.demo.dto;

import com.example.demo.entity.Order_status;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class OrderDTO {
    public OrderDTO(Long id, Long customerId, Long tableId, BigDecimal totalPrice, OffsetDateTime createdAt, OffsetDateTime updatedAt, List<Long> orderItemIds, List<Long> orderStatusChangeIds, List<Long> reviewIds) {
        this.id = id;
        this.customerId = customerId;
        this.tableId = tableId;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderItemIds = orderItemIds;
        this.orderStatusChangeIds = orderStatusChangeIds;
        this.reviewIds = reviewIds;
    }

    Long id;
    Long customerId;
    Long tableId;

    public OrderDTO() {
    }

    BigDecimal totalPrice;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
    List<Long> orderItemIds;
    List<Long> orderStatusChangeIds;
    List<Long> reviewIds;
    String status;


    public OrderDTO(Long id, Long customerId, Long tableId, BigDecimal totalPrice, OffsetDateTime createdAt, OffsetDateTime updatedAt, List<Long> orderItemIds, List<Long> orderStatusChangeIds, List<Long> reviewIds, Order_status status) {
        this.id = id;
        this.customerId = customerId;
        this.tableId = tableId;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderItemIds = orderItemIds;
        this.orderStatusChangeIds = orderStatusChangeIds;
        this.reviewIds = reviewIds;
        this.status = status.toString();
    }


    public OrderDTO(Long customerId, Long tableId, BigDecimal totalPrice) {
        this.customerId = customerId;
        this.tableId = tableId;
        this.totalPrice = totalPrice;

    }
}
