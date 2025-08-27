package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.entity.Order_status;
import com.example.demo.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/cashier/order/status")
public class OrderStatusController {
    @Autowired
    private  OrderStatusService orderStatusService;
    @PutMapping("/update")
    public ResponseEntity<Order> updateStatus(@RequestParam Long orderId,@RequestParam Order_status newStatus) {

        return ResponseEntity.ok(orderStatusService.updateOrderStatus(orderId, newStatus));
    }




}
