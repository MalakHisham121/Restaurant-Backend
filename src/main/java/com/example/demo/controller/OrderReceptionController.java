package com.example.demo.controller;

import com.example.demo.dto.OrderReceptionDTO;
import com.example.demo.dto.OrderStatusUpdateDTO;
import com.example.demo.entity.Order_status;
import com.example.demo.service.OrderReceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/order/history")
@PreAuthorize("hasRole('ADMIN')")
public class OrderReceptionController {

    @Autowired
    private OrderReceptionService orderReceptionService;

    @GetMapping("/list")
    public ResponseEntity<?> getOrderHistoryList(
            @RequestParam(value = "date", required = false) String dateStr,
            @RequestParam(value = "status", required = false) String status
    ) {
        try {
            OffsetDateTime dateTime = null;
            if (dateStr != null && !dateStr.trim().isEmpty()) {
                LocalDate date = parseFlexibleDate(dateStr);

                // Instead of forcing UTC, use the system default timezone
                // This is more likely to match how dates are stored in the database
                dateTime = date.atStartOfDay().atOffset(OffsetDateTime.now().getOffset());
            }

            List<OrderReceptionDTO> orders = orderReceptionService.getOrderHistoryList(dateTime, status);
            return ResponseEntity.ok(orders);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid date format. Supported formats: YYYY-MM-DD, MMM DD, or MM-DD (e.g., 2025-08-24, Aug 23, 08-23)");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request: " + e.getMessage());
        }
    }

    @GetMapping("/view")
    public ResponseEntity<?> getOrderById(@RequestParam("orderId") Long orderId) {
        try {
            OrderReceptionDTO order = orderReceptionService.getOrderById(orderId);

            if (order == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(order);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the order: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrderStatus(@RequestBody OrderStatusUpdateDTO updateRequest) {
        try {
            if (updateRequest.getOrderId() == null) {
                return ResponseEntity.badRequest().body("Order ID is required");
            }

            if (updateRequest.getStatus() == null || updateRequest.getStatus().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Status is required");
            }

            String result = orderReceptionService.updateOrderStatus(updateRequest.getOrderId(), Order_status.valueOf(updateRequest.getStatus().trim()));

            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found with ID: " + updateRequest.getOrderId());
            }

            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the order status: " + e.getMessage());
        }
    }

    private LocalDate parseFlexibleDate(String dateStr) throws DateTimeParseException {
        dateStr = dateStr.trim();

        // Try YYYY-MM-DD format first (e.g., "2025-08-20")
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException ignored) {}

        // Try MMM DD format (e.g., "Aug 23")
        try {
            String fullDateStr = dateStr + " " + LocalDate.now().getYear();
            DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
            return LocalDate.parse(fullDateStr, fullFormatter);
        } catch (DateTimeParseException ignored) {}

        // Try MMM D format with single digit day (e.g., "Aug 3")
        try {
            String fullDateStr = dateStr + " " + LocalDate.now().getYear();
            DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("MMM d yyyy");
            return LocalDate.parse(fullDateStr, fullFormatter);
        } catch (DateTimeParseException ignored) {}

        // Try MM-DD format (e.g., "08-23")
        try {
            String[] parts = dateStr.split("-");
            if (parts.length == 2) {
                int month = Integer.parseInt(parts[0]);
                int day = Integer.parseInt(parts[1]);
                return LocalDate.of(LocalDate.now().getYear(), month, day);
            }
        } catch (NumberFormatException ignored) {}

        // Try YYYY-MM format (e.g., "2025-08") - assume first day of month
        try {
            String[] parts = dateStr.split("-");
            if (parts.length == 2 && parts[0].length() == 4) {
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                return LocalDate.of(year, month, 1);
            }
        } catch (NumberFormatException ignored) {}

        throw new DateTimeParseException("Unable to parse date: " + dateStr, dateStr, 0);
    }
}
