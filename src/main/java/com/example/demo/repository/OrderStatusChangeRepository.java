package com.example.demo.repository;

import com.example.demo.entity.OrderStatusChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusChangeRepository extends JpaRepository<OrderStatusChange, Long> {
}
