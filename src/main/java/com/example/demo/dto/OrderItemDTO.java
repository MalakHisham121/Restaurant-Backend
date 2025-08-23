package com.example.demo.dto;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Long id;
    private Long orderid;
    private Long productid;
    private Integer quantity;
    private BigDecimal price;
    public OrderItemDTO(){}
    public OrderItemDTO(Long id,Long orderid,Long productid,int quantity,BigDecimal price){
        this.id = id;
        this.orderid = orderid;
        this.price = price;
        this.productid = productid;
        this.quantity = quantity;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderid;
    }

    public void setOrderId(Long orderId) {
        this.orderid = orderId;
    }

    public Long getProductId() {
        return productid;
    }

    public void setProductId(Long productId) {
        this.productid = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


}
