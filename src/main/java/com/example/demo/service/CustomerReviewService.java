package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.Review;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public CustomerReviewService(ReviewRepository reviewRepository, OrderRepository orderRepository ,UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }


    public Review createReview(Long orderId, Long customerId, ReviewDTO reviewDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        Review review = new Review();
        review.setOrder(order);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        if (customerId != null) {
            userRepository.findById(customerId).ifPresent(review::setCustomer);
        } else {
            review.setCustomer(null);
        }

        review.setVisible(true);

        return reviewRepository.save(review);
    }


    //Get all review in the database
    public Optional<Review> GetReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    //Get all review for the specific customer
    public List<Review> getReviewsByCustomer(Long customerId) {
        return reviewRepository.findByCustomerId(customerId);
    }

    // Update
    public Optional<Review> updateReview(Long reviewId, Long customerId, Review updatedReview) {
        return reviewRepository.findById(reviewId)
                .filter(review -> review.getCustomer() != null && review.getCustomer().getId().equals(customerId))
                .map(existingReview -> {
                    existingReview.setRating(updatedReview.getRating());
                    existingReview.setComment(updatedReview.getComment());
                    return reviewRepository.save(existingReview);
                });
    }

    // Delete
    public boolean deleteReview(Long reviewId, Long customerId) {
        return reviewRepository.findById(reviewId)
                .filter(review -> review.getCustomer() != null && review.getCustomer().getId().equals(customerId))
                .map(review -> {
                    reviewRepository.delete(review);
                    return true;
                })
                .orElse(false);
    }

}
