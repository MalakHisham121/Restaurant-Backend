package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    public Review createReview(Long orderId, ReviewDTO reviewDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Review review = new Review();
        review.setOrder(order);
        review.setCustomer(customer);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

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
    public Review updateReview(Long reviewId, Review updatedReview) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        if (review.getCustomer() == null || !review.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("You are not allowed to update this review");
        }

        review.setRating(updatedReview.getRating());
        review.setComment(updatedReview.getComment());
        return reviewRepository.save(review);
    }

    // Delete
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));

        // Get current logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        // Only allow the owner to delete
        if (review.getCustomer() == null || !review.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("You are not allowed to delete this review");
        }

        reviewRepository.delete(review);
    }

}
