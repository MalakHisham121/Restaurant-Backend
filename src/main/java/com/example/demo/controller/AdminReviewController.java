package com.example.demo.controller;

import com.example.demo.dto.AdminReviewDTO;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.service.AdminReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {
    private final AdminReviewService adminReviewService;

    public AdminReviewController(AdminReviewService adminReviewService) {
        this.adminReviewService = adminReviewService;
    }

    private AdminReviewDTO mapToDTO(Review review) {
        AdminReviewDTO dto = new AdminReviewDTO();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());

        if (review.getOrder() != null) {
            dto.setOrderId(review.getOrder().getId());
        }

        try {
            User customer = review.getCustomer();
            if (customer != null) {
                dto.setCustomerName(customer.getUsername() != null
                        ? customer.getUsername()
                        : "Unknown User");
            } else {
                dto.setCustomerName("Unknown User");
            }
        } catch (Exception e) {
            // لو حصل EntityNotFoundException هنا
            dto.setCustomerName("Deleted User");
        }

        return dto;
    }

    @GetMapping("/list")
    public List<AdminReviewDTO> listAllReviews() {
        return adminReviewService.listAllReviews()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{id}/toggle-visibility")
    public ResponseEntity<Map<String, Object>> toggleVisibility(@PathVariable Long id) {
        Review updatedReview = adminReviewService.toggleReviewVisibility(id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Review visibility updated successfully");
        response.put("reviewId", updatedReview.getId());
        response.put("visible", updatedReview.isVisible());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReview(@PathVariable Long id) {
        adminReviewService.DeleteReview(id);
    }
}
