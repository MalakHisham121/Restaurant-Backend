package com.example.demo.controller;

import com.example.demo.dto.OrderItemDTO;
import com.example.demo.entity.OrderItem;
import com.example.demo.service.OrderItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orderitem")
public class OrderItemController {
    private final OrderItemService orderItemService;
    public OrderItemController (OrderItemService orderItemService){
        this.orderItemService = orderItemService;
    }
@GetMapping("")
    public List<OrderItemDTO>getByOrderId(@RequestParam Long orderId){
        return orderItemService.findByOrderId(orderId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }
    private OrderItemDTO mapToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        return dto;
    }
}

