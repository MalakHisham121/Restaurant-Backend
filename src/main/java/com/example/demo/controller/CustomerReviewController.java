package com.example.demo.controller;


import com.example.demo.dto.ReviewDTO;
import com.example.demo.entity.Review;
import com.example.demo.service.CustomerReviewService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer/review")
public class CustomerReviewController {
    private final CustomerReviewService  reviewService;;

    public CustomerReviewController(CustomerReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<ReviewDTO> createReview(
            @RequestParam Long orderId,
            @RequestParam(required = false) Long customerId,
            @RequestBody ReviewDTO reviewDTO) {

        Review createdReview = reviewService.createReview(orderId, customerId, reviewDTO);

        ReviewDTO response = mapToDTO(createdReview);
        return ResponseEntity.ok(response);
    }

    private ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());

        // Check for null before accessing
        if (review.getOrder() != null) {
            dto.setOrderId(review.getOrder().getId());
        }

        if (review.getCustomer() != null) {
            dto.setCustomerId(review.getCustomer().getId());
        }

        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        return dto;
    }



    @GetMapping("/read/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        Optional<Review> review = reviewService.GetReviewById(id);
        return review.map(value -> ResponseEntity.ok(mapToDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }
    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByCustomer(@PathVariable Long customerId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByCustomer(customerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long id,
            @RequestParam Long customerId,
            @RequestBody ReviewDTO updatedReviewDTO) {

        Review updatedReview = new Review();
        updatedReview.setRating(updatedReviewDTO.getRating());
        updatedReview.setComment(updatedReviewDTO.getComment());

        Optional<Review> updated = reviewService.updateReview(id, customerId, updatedReview);

        return updated.map(value -> ResponseEntity.ok(mapToDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            @RequestParam Long customerId) {

        boolean deleted = reviewService.deleteReview(id, customerId);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
