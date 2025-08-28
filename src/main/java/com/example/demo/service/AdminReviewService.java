package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
public class AdminReviewService {
    private final ReviewRepository reviewRepository;


    //Dependency Injection on the constructor of  AdminReviewService to be
    //apple to use the ReviewRepository to access dataBase
    public AdminReviewService(ReviewRepository reviewRepo) {
        this.reviewRepository = reviewRepo;
    }

    //list of all reviews using the built in methods in the JPA of the reviewRepository
    public List<Review> listAllReviews() {
        return reviewRepository.findAll();
    }

    @Transactional
    public Review toggleReviewVisibility(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review with ID " + id + " not found"));

        review.setVisible(!review.isVisible());
        return reviewRepository.save(review);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    //we used reviewRepository.existsById(id) because it only return T or F
    //not the whole object but Optional<Review> Reviews return the whole object
    public void DeleteReview(Long id) {
        Optional<Review> Reviews = reviewRepository.findById(id);
        if(reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("Review not found");
        }
    }
}
