package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.Order_status;
import com.example.demo.repository.OrderRepo;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusService {
    private final OrderRepo orderRepo;
    OrderStatusService(OrderRepo orderRepo){
        this.orderRepo = orderRepo;
    }

    public Order updateOrderStatus(Long orderId, Order_status newStatus) {
        if (!orderRepo.existsById(orderId)) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        orderRepo.updateOrderStatus(orderId, newStatus);

        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order with requested ID not found"));    }
}
