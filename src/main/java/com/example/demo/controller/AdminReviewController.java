package com.example.demo.controller;


import com.example.demo.dto.AdminReviewDTO;
import com.example.demo.entity.Review;
import com.example.demo.service.AdminReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {
    private final AdminReviewService adminReviewService;

    public AdminReviewController(AdminReviewService adminReviewService) {
        this.adminReviewService = adminReviewService;
    }
    private AdminReviewDTO mapToDTO(Review review){
        AdminReviewDTO dto = new AdminReviewDTO();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());

        //if the customer place an order befor making an review
        if(review.getOrder() != null){
            dto.setOrderId(review.getOrder().getId());
        }

        if (review.getCustomer() != null) {
            dto.setCustomerId(review.getCustomer().getId());
            dto.setCustomerName(review.getCustomer().getName());
        }

        return dto;
    }

    @GetMapping("/list")
    public List<AdminReviewDTO> listAllReviews() {
        List<AdminReviewDTO> reviews = adminReviewService.listAllReviews()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return reviews;
    }



    @PutMapping("/update/{id}")
    public AdminReviewDTO updateReview(
            @PathVariable Long id,
            @RequestBody AdminReviewDTO updatedReviewDTO) {

        Review updatedEntity = new Review();
        updatedEntity.setRating(updatedReviewDTO.getRating());
        updatedEntity.setComment(updatedReviewDTO.getComment());

        Review review = adminReviewService.updateReview(id, updatedEntity);
        return mapToDTO(review);

    }

    @DeleteMapping("/delete/{id}")
    public void deleteReview(@PathVariable Long id) {
        adminReviewService.DeleteReview(id);
    }
}
