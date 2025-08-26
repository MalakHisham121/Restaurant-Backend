package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends BaseRepository<Order,Long> {
}
