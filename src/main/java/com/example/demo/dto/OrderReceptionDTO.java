package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class OrderReceptionDTO {
    private Long orderId;
    private String customerName;
    private List<ItemDTO> items;
    private BigDecimal total;
    private OffsetDateTime date;
    private String status;

    public static class ItemDTO {
        private String productName;
        private Integer quantity;
        private BigDecimal price;

        public ItemDTO(String productName, Integer quantity, BigDecimal price) {
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }
        public String getProductName() { return productName; }
        public Integer getQuantity() { return quantity; }
        public BigDecimal getPrice() { return price; }
        public void setProductName(String productName) { this.productName = productName; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }

    public OrderReceptionDTO() {}
    public OrderReceptionDTO(Long orderId, String customerName, List<ItemDTO> items, BigDecimal total, OffsetDateTime date, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = items;
        this.total = total;
        this.date = date;
        this.status = status;
    }
    public Long getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public List<ItemDTO> getItems() { return items; }
    public BigDecimal getTotal() { return total; }
    public OffsetDateTime getDate() { return date; }
    public String getStatus() { return status; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setItems(List<ItemDTO> items) { this.items = items; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public void setDate(OffsetDateTime date) { this.date = date; }
    public void setStatus(String status) { this.status = status; }
}
