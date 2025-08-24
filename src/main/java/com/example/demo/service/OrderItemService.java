package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.OrderItemRepo;
import com.example.demo.repository.OrderRepo;
import com.example.demo.repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    private final OrderItemRepo orderItemRepo;
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    public OrderItemService(OrderItemRepo orderItemRepo,OrderRepo orderRepo,ProductRepo productRepo){
        this.orderItemRepo = orderItemRepo;
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;

    }
    @Transactional
    public List<OrderItem> findByOrderId(Long orderId){

        return orderItemRepo.findByOrderId(orderId);
    }
    @Transactional
    public Order addOrderItem(OrderItemDTO orderItem){
        if(orderItem.getOrderId() == null|| orderItem.getProductId()==null|| orderItem.getQuantity()==null|| orderItem.getPrice() == null) {
            throw new IllegalArgumentException("Your passed order item is not valid , product id ,order id, quantity and price connot be null ");
        }
       // Order o= orderRepo.findById(orderItem.getOrderId()).get();

            Order order = orderRepo.findById(orderItem.getOrderId()). orElseThrow(()->new RuntimeException("Order not found with ID "+ orderItem.getOrderId()));



        OrderItem item = new OrderItem();
        item.setOrder(order);
        Product product =  productRepo.findById(orderItem.getProductId()).orElseThrow(()->new RuntimeException("Product not found"));
            item.setProduct(product);
            item.setQuantity(orderItem.getQuantity());
            item.setPrice(calcPrice(orderItem.getQuantity(),orderItem.getPrice()));


        orderItemRepo.save(item);

//        order.getOrderItems().add(item);
//        orderRepo.save(order);
        totalPrice(order);

        return order;

    }
    private BigDecimal calcPrice(int quantity, BigDecimal p) {
        return p.multiply(BigDecimal.valueOf(quantity));
    }
    private void totalPrice (Order order) {

       BigDecimal total = order.getOrderItems().stream()
                .map(oi -> oi.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);
        orderRepo.save(order);

    }

    private OrderItemDTO mapToOrderItemDTO(OrderItem item) {
        return new OrderItemDTO(
                item.getId(),
                item.getOrder().getId(),
                item.getProduct().getId(),
                item.getQuantity(),
                item.getPrice()
        );
    }
    private OrderDTO mapToOrderDTO(Order order) {
        List<Long> orderItemIds = order.getOrderItems().stream()
                .map(OrderItem::getId)
                .collect(Collectors.toList());
        List<Long> orderStatusChangeIds = order.getOrderStatusChanges().stream()
                .map(OrderStatusChange::getId)
                .collect(Collectors.toList());
        List<Long> reviewIds = order.getReviews().stream()
                .map(Review::getId)
                .collect(Collectors.toList());
        order.setUpdatedAt(OffsetDateTime.now());
        order.setCreatedAt(OffsetDateTime.now());
        return new OrderDTO(
                order.getId(),
                order.getCustomer().getId(),
                order.getTable().getId(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                orderItemIds,
                orderStatusChangeIds,
                reviewIds,
                Order_status.PENDING
        );
    }
}
