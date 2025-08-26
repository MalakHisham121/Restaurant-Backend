package com.example.demo.service;

import com.example.demo.dto.NotificationResponseDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderStatusChange;
import com.example.demo.repository.OrderReceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CustomerNotificationService {

    @Autowired
    private OrderReceptionRepository orderRepository;

    public NotificationResponseDTO checkForReadyOrders(Long customerId) {
        // Get all orders for the customer with details
        List<Order> customerOrders = orderRepository.findAllWithDetails().stream()
                .filter(order -> order.getCustomer() != null && order.getCustomer().getId().equals(customerId))
                .toList();

        // Check if any order has "ready" status
        boolean hasReadyOrder = customerOrders.stream()
                .anyMatch(order -> {
                    // Check the latest status from OrderStatusChanges first
                    String latestStatus = order.getOrderStatusChanges().stream()
                            .max(Comparator.comparing(OrderStatusChange::getCreatedAt))
                            .map(OrderStatusChange::getStatus)
                            .orElse(order.getStatus());

                    return "ready".equalsIgnoreCase(latestStatus);
                });

        if (hasReadyOrder) {
            return new NotificationResponseDTO("Your order is ready for pickup!");
        } else {
            return new NotificationResponseDTO("No ready orders at this time.");
        }
    }

    // Alternative method to check for ready orders by table ID (if customer doesn't have an account)
    public NotificationResponseDTO checkForReadyOrdersByTable(Long tableId) {
        // Get all orders for the table with details
        List<Order> tableOrders = orderRepository.findAllWithDetails().stream()
                .filter(order -> order.getTable() != null && order.getTable().getId().equals(tableId))
                .toList();

        // Check if any recent order has "ready" status
        boolean hasReadyOrder = tableOrders.stream()
                .anyMatch(order -> {
                    // Check the latest status from OrderStatusChanges first
                    String latestStatus = order.getOrderStatusChanges().stream()
                            .max(Comparator.comparing(OrderStatusChange::getCreatedAt))
                            .map(OrderStatusChange::getStatus)
                            .orElse(order.getStatus());

                    return "ready".equalsIgnoreCase(latestStatus);
                });

        if (hasReadyOrder) {
            return new NotificationResponseDTO("Your order is ready for pickup!");
        } else {
            return new NotificationResponseDTO("No ready orders at this time.");
        }
    }
}
