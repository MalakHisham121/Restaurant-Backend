package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.Review;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public CustomerReviewService(ReviewRepository reviewRepository, OrderRepository orderRepository ) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
    }

    public Review CreateReview(Long orderId,Long customerId ,Review review) {
        Optional<Order> order = orderRepository.findById(orderId);

        if(order.isEmpty() || !order.get().getCustomer().getId().equals(customerId)) {
            throw new IllegalArgumentException("You must Place an Order before Leaving a review.");
        }

        review.setOrder(order.get());
        review.setCustomer(order.get().getCustomer());
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

    //Update
    public Optional<Review> updateReview(Long reviewId, Long customerId, Review updatedReview) {
        return reviewRepository.findById(reviewId)
                .filter(review -> review.getCustomer().getId().equals(customerId))
                .map(existingReview -> {
                    existingReview.setRating(updatedReview.getRating());
                    existingReview.setComment(updatedReview.getComment());
                    return reviewRepository.save(existingReview);
                });
    }

    // Delete
    public boolean deleteReview(Long reviewId, Long customerId) {
        return reviewRepository.findById(reviewId)
                .filter(review -> review.getCustomer().getId().equals(customerId))
                .map(review -> {
                    reviewRepository.delete(review);
                    return true;
                })
                .orElse(false);
    }

}
