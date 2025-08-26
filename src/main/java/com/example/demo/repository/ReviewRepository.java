package com.example.demo.repository;
import java.util.List;
import com.example.demo.entity.Review;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends BaseRepository<Review, Long> {
    List<Review> findByCustomerId(long CustomerId );
    List<Review> findByOrderId(long orderId);
}
