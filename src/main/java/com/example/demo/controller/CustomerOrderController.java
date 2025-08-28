package com.example.demo.controller;

import com.example.demo.dto.OrderPlacementDTO;
import com.example.demo.dto.OrderPlacementResponseDTO;
import com.example.demo.dto.OrderCancelDTO;
import com.example.demo.service.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/order")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody OrderPlacementDTO orderPlacementDTO,
                                       Authentication authentication) {
        try {
            // Validate request
            if (orderPlacementDTO.getTableId() == null) {
                return ResponseEntity.badRequest().body("Table ID is required");
            }

            if (orderPlacementDTO.getItems() == null || orderPlacementDTO.getItems().isEmpty()) {
                return ResponseEntity.badRequest().body("Order items are required");
            }

            // Validate each item
            for (OrderPlacementDTO.ItemDTO item : orderPlacementDTO.getItems()) {
                if (item.getId() == null) {
                    return ResponseEntity.badRequest().body("Product ID is required for all items");
                }
                if (item.getQty() == null || item.getQty() <= 0) {
                    return ResponseEntity.badRequest().body("Quantity must be greater than 0 for all items");
                }
            }

            // Get the authenticated customer's username
            String customerUsername = authentication.getName();

            OrderPlacementResponseDTO response = customerOrderService.placeOrder(orderPlacementDTO, customerUsername);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your order: " + e.getMessage());
        }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestBody OrderCancelDTO orderCancelDTO,
                                        Authentication authentication) {
        try {
            // Validate request
            if (orderCancelDTO.getOrderId() == null) {
                return ResponseEntity.badRequest().body("Order ID is required");
            }

            // Get the authenticated customer's username
            String customerUsername = authentication.getName();

            String result = customerOrderService.cancelOrder(orderCancelDTO.getOrderId(), customerUsername);
            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while cancelling your order: " + e.getMessage());
        }
    }
}
