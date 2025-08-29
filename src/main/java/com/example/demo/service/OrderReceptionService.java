package com.example.demo.service;

import com.example.demo.dto.OrderReceptionDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderStatusChange;
import com.example.demo.entity.Order_status;
import com.example.demo.repository.OrderReceptionRepository;
import com.example.demo.repository.OrderStatusChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderReceptionService {

    @Autowired
    private OrderReceptionRepository orderReceptionRepository;

    @Autowired
    private OrderStatusChangeRepository orderStatusChangeRepository;

    public List<OrderReceptionDTO> getOrderHistoryList(OffsetDateTime date, String status) {
        List<Order> orders;

        // Clean up the status parameter to handle whitespace and null
        String cleanStatus = status != null ? status.trim() : null;
        if (cleanStatus != null && cleanStatus.isEmpty()) {
            cleanStatus = null;
        }

        if (date != null && cleanStatus != null) {
            // Both date and status provided
            OffsetDateTime startDate = date;
            OffsetDateTime endDate = date.plusDays(1);
            orders = orderReceptionRepository.findByDateRangeAndStatus(startDate, endDate, cleanStatus);
        } else if (date != null) {
            // Only date provided
            OffsetDateTime startDate = date;
            OffsetDateTime endDate = date.plusDays(1);
            orders = orderReceptionRepository.findByDateRange(startDate, endDate);
        } else if (cleanStatus != null) {
            // Only status provided
            orders = orderReceptionRepository.findByStatus(cleanStatus);
        } else {
            // No filters provided
            orders = orderReceptionRepository.findAllWithDetails();
        }

        return orders.stream().map(this::mapOrderToDTO).collect(Collectors.toList());
    }

    public OrderReceptionDTO getOrderById(Long orderId) {
        Order order = orderReceptionRepository.findByIdWithDetails(orderId);
        if (order == null) {
            return null;
        }
        return mapOrderToDTO(order);
    }

    public String updateOrderStatus(Long orderId, Order_status status) {
        Order order = orderReceptionRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }

        // Update the main order status as well
        order.setStatus(status);

        // Create new order status change
        OrderStatusChange statusChange = new OrderStatusChange();
        statusChange.setOrder(order);
        statusChange.setStatus(String.valueOf(status));
        statusChange.setCreatedAt(OffsetDateTime.now());

        // Update order's updated timestamp
        order.setUpdatedAt(OffsetDateTime.now());

        // Save both entities
        orderStatusChangeRepository.save(statusChange);
        orderReceptionRepository.save(order);

        return "Order status updated successfully to: " + status;
    }

    private OrderReceptionDTO mapOrderToDTO(Order order) {
        String customerName = order.getCustomer() != null ? order.getCustomer().getUsername() : "Unknown Customer";

        List<OrderReceptionDTO.ItemDTO> items = order.getOrderItems().stream()
                .map(item -> new OrderReceptionDTO.ItemDTO(
                        item.getProduct() != null ? item.getProduct().getName() : "Unknown Product",
                        item.getQuantity(),
                        item.getPrice()
                )).collect(Collectors.toList());

        // Try to get the latest status from OrderStatusChanges first, fallback to order.status
        String latestStatus = order.getOrderStatusChanges().stream()
                .max(Comparator.comparing(OrderStatusChange::getCreatedAt))
                .map(OrderStatusChange::getStatus)
                .orElse(order.getStatus() != null ? String.valueOf(order.getStatus()) : "Unknown");

        return new OrderReceptionDTO(
                order.getId(),
                customerName,
                items,
                order.getTotalPrice(),
                order.getCreatedAt(),
                latestStatus
        );
    }
}
