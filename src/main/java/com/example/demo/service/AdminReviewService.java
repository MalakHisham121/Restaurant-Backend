package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;
import org.springframework.stereotype.Service;

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

    //get the old review by id then update(rate, comment) it with new one
    public Review updateReview(Long id , Review updatedReview) {
        Optional<Review> Reviews = reviewRepository.findById(id);

        if(Reviews.isPresent()) {
            Review review = Reviews.get();
            review.setRating(updatedReview.getRating());
            review.setComment(updatedReview.getComment());
            return reviewRepository.save(review);
        }
        else {
            throw new RuntimeException("Review not found");
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
