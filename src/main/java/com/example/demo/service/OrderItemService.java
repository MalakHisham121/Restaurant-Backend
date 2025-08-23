package com.example.demo.service;

import com.example.demo.entity.OrderItem;
import com.example.demo.repository.OrderItemRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepo orderItemRepo;
    public OrderItemService(OrderItemRepo orderItemRepo){
        this.orderItemRepo = orderItemRepo;

    }
    @Transactional
    public List<OrderItem> findByOrderId(Long orderId){

        return orderItemRepo.findByOrderId(orderId);
    }
}
