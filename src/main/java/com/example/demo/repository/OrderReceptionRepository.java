package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderReceptionRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.customer " +
           "LEFT JOIN FETCH o.orderItems oi " +
           "LEFT JOIN FETCH oi.product " +
           "LEFT JOIN FETCH o.orderStatusChanges")
    List<Order> findAllWithDetails();

    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.customer " +
           "LEFT JOIN FETCH o.orderItems oi " +
           "LEFT JOIN FETCH oi.product " +
           "LEFT JOIN FETCH o.orderStatusChanges " +
           "WHERE o.createdAt >= :startDate AND o.createdAt < :endDate")
    List<Order> findByDateRange(@Param("startDate") OffsetDateTime startDate, @Param("endDate") OffsetDateTime endDate);

    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.customer " +
           "LEFT JOIN FETCH o.orderItems oi " +
           "LEFT JOIN FETCH oi.product " +
           "LEFT JOIN FETCH o.orderStatusChanges " +
           "WHERE LOWER(o.status) = LOWER(:status)")
    List<Order> findByStatus(@Param("status") String status);

    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.customer " +
           "LEFT JOIN FETCH o.orderItems oi " +
           "LEFT JOIN FETCH oi.product " +
           "LEFT JOIN FETCH o.orderStatusChanges " +
           "WHERE (o.createdAt >= :startDate AND o.createdAt < :endDate) " +
           "AND LOWER(o.status) = LOWER(:status)")
    List<Order> findByDateRangeAndStatus(@Param("startDate") OffsetDateTime startDate,
                                        @Param("endDate") OffsetDateTime endDate,
                                        @Param("status") String status);

    // Method to find a single order by ID with all details
    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.customer " +
           "LEFT JOIN FETCH o.orderItems oi " +
           "LEFT JOIN FETCH oi.product " +
           "LEFT JOIN FETCH o.orderStatusChanges " +
           "WHERE o.id = :orderId")
    Order findByIdWithDetails(@Param("orderId") Long orderId);

    @Query("SELECT DISTINCT o.status FROM Order o WHERE o.status IS NOT NULL ORDER BY o.status")
    List<String> findAllDistinctStatuses();
}
