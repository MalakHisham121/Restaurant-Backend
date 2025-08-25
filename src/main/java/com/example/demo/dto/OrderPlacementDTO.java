package com.example.demo.dto;

import java.util.List;

public class OrderPlacementDTO {
    private Long tableId;
    private List<ItemDTO> items;

    public OrderPlacementDTO() {}

    public OrderPlacementDTO(Long tableId, List<ItemDTO> items) {
        this.tableId = tableId;
        this.items = items;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public static class ItemDTO {
        private Long id;
        private Integer qty;

        public ItemDTO() {}

        public ItemDTO(Long id, Integer qty) {
            this.id = id;
            this.qty = qty;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
        }
    }
}
