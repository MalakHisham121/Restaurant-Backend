package com.example.demo.service;

import com.example.demo.dto.OrderTrackingDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderStatusChange;
import com.example.demo.repository.OrderReceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

@Service
public class OrderTrackingService {

    @Autowired
    private OrderReceptionRepository orderRepository;

    public OrderTrackingDTO trackOrder(Long orderId) {
        // Find the order with all details
        Order order = orderRepository.findByIdWithDetails(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        // Get the latest status from OrderStatusChanges first, fallback to order.status
        String currentStatus = order.getOrderStatusChanges().stream()
                .max(Comparator.comparing(OrderStatusChange::getCreatedAt))
                .map(OrderStatusChange::getStatus)
                .orElse(order.getStatus() != null ? String.valueOf(order.getStatus()) : "Unknown");

        // Calculate estimated time based on status and order creation time
        String estimatedTime = calculateEstimatedTime(order, currentStatus);

        return new OrderTrackingDTO(orderId, currentStatus, estimatedTime);
    }

    private String calculateEstimatedTime(Order order, String status) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime orderCreatedAt = order.getCreatedAt();

        switch (status.toLowerCase()) {
            case "pending":
                return "5-10 minutes";

            case "confirmed":
            case "preparing":
                // Calculate time since order was placed
                long minutesSinceOrder = ChronoUnit.MINUTES.between(orderCreatedAt, now);
                int totalItems = order.getOrderItems().size();

                // Base time: 15 minutes + 3 minutes per item
                int totalEstimatedMinutes = 15 + (totalItems * 3);
                int remainingMinutes = Math.max(totalEstimatedMinutes - (int)minutesSinceOrder, 2);

                return remainingMinutes + " minutes";

            case "ready":
                return "Ready for pickup";

            case "completed":
                return "Order completed";

            case "cancelled":
                return "Order cancelled";

            default:
                return "Time unavailable";
        }
    }
}
