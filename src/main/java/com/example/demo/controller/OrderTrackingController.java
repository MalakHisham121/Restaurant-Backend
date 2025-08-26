package com.example.demo.controller;

import com.example.demo.dto.OrderTrackingDTO;
import com.example.demo.service.OrderTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/backend/order")
public class OrderTrackingController {

    @Autowired
    private OrderTrackingService orderTrackingService;

    @GetMapping("/track")
    public ResponseEntity<?> trackOrder(@RequestParam("orderId") Long orderId) {
        try {
            if (orderId == null) {
                return ResponseEntity.badRequest().body("Order ID is required");
            }

            OrderTrackingDTO trackingInfo = orderTrackingService.trackOrder(orderId);
            return ResponseEntity.ok(trackingInfo);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while tracking the order: " + e.getMessage());
        }
    }
}
