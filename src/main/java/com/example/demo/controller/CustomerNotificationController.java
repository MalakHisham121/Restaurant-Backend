package com.example.demo.controller;

import com.example.demo.dto.NotificationResponseDTO;
import com.example.demo.service.CustomerNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/notification")
public class CustomerNotificationController {

    @Autowired
    private CustomerNotificationService customerNotificationService;

    @GetMapping("/receive")
    public ResponseEntity<?> receiveNotification(
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "tableId", required = false) Long tableId) {
        try {
            NotificationResponseDTO response;

            // Check by customer ID first (if user is logged in)
            if (customerId != null) {
                response = customerNotificationService.checkForReadyOrders(customerId);
            }
            // Fallback to table ID (for guests)
            else if (tableId != null) {
                response = customerNotificationService.checkForReadyOrdersByTable(tableId);
            }
            else {
                return ResponseEntity.badRequest()
                        .body("Either customerId or tableId parameter is required");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while checking notifications: " + e.getMessage());
        }
    }
}
