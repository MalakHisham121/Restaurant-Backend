package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository  extends BaseRepository<Order, Long>{
    List<Order> findByCustomerId(Long customerId);

    boolean existsByIdAndCustomerId(Long orderId, Long customerId);
}
