package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.Order_status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends BaseRepository<Order,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :newStatus WHERE o.id = :orderId")
    void updateOrderStatus(Long orderId, Order_status newStatus);
}
