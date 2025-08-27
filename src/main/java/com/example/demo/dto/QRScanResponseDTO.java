package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public class QRScanResponseDTO {
    private Long tableId;
    private List<CategoryWithItemsDTO> categories;

    public QRScanResponseDTO() {}

    public QRScanResponseDTO(Long tableId, List<CategoryWithItemsDTO> categories) {
        this.tableId = tableId;
        this.categories = categories;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public List<CategoryWithItemsDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryWithItemsDTO> categories) {
        this.categories = categories;
    }

    public static class CategoryWithItemsDTO {
        private Long id;
        private String name;
        private List<ProductItemDTO> items;

        public CategoryWithItemsDTO() {}

        public CategoryWithItemsDTO(Long id, String name, List<ProductItemDTO> items) {
            this.id = id;
            this.name = name;
            this.items = items;
        }

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public List<ProductItemDTO> getItems() { return items; }
        public void setItems(List<ProductItemDTO> items) { this.items = items; }
    }

    public static class ProductItemDTO {
        private Long id;
        private String name;
        private String description;
        private String imageUrl;
        private BigDecimal price;

        public ProductItemDTO() {}

        public ProductItemDTO(Long id, String name, String description, String imageUrl, BigDecimal price) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.imageUrl = imageUrl;
            this.price = price;
        }

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }
}
