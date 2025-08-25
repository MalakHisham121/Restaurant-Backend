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
           "WHERE EXISTS (SELECT osc FROM OrderStatusChange osc WHERE osc.order = o AND " +
           "(LOWER(osc.status) = LOWER(:status) OR " +
           "LOWER(osc.status) LIKE LOWER(CONCAT('%', :status, '%'))))")
    List<Order> findByStatus(@Param("status") String status);

    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.customer " +
           "LEFT JOIN FETCH o.orderItems oi " +
           "LEFT JOIN FETCH oi.product " +
           "LEFT JOIN FETCH o.orderStatusChanges " +
           "WHERE (o.createdAt >= :startDate AND o.createdAt < :endDate) " +
           "AND EXISTS (SELECT osc FROM OrderStatusChange osc WHERE osc.order = o AND " +
           "(LOWER(osc.status) = LOWER(:status) OR " +
           "LOWER(osc.status) LIKE LOWER(CONCAT('%', :status, '%'))))")
    List<Order> findByDateRangeAndStatus(@Param("startDate") OffsetDateTime startDate,
                                        @Param("endDate") OffsetDateTime endDate,
                                        @Param("status") String status);

    // Add a helper method to get all distinct statuses for debugging
    @Query("SELECT DISTINCT osc.status FROM OrderStatusChange osc ORDER BY osc.status")
    List<String> findAllDistinctStatuses();

    // Method to find a single order by ID with all details
    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.customer " +
           "LEFT JOIN FETCH o.orderItems oi " +
           "LEFT JOIN FETCH oi.product " +
           "LEFT JOIN FETCH o.orderStatusChanges " +
           "WHERE o.id = :orderId")
    Order findByIdWithDetails(@Param("orderId") Long orderId);
}
