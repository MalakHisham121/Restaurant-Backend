package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.service.OrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('CUSTOMER')")
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
    @PostMapping("/add")
    public ResponseEntity<Order> addOrderItem(@RequestBody OrderItemDTO request) {
       return ResponseEntity.status(HttpStatus.CREATED).body(orderItemService.addOrderItem(request));
    }
    private OrderItemDTO mapToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        return dto;
    }
    @GetMapping("/update")
    public  ResponseEntity<OrderItem> updateOrderItemQuantity(@RequestParam long orderItemId,@RequestParam int quantity){
        return ResponseEntity.status(HttpStatus.OK).body(orderItemService.updateOrderItemQuantity(orderItemId,quantity));

    }
    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<String> deleteOrderitem(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(orderItemService.deleteOrderItem(id));

    }

    




}

