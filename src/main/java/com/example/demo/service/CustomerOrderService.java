package com.example.demo.service;

import com.example.demo.dto.OrderPlacementDTO;
import com.example.demo.dto.OrderPlacementResponseDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.OrderStatusChange;
import com.example.demo.entity.Product;
import com.example.demo.entity.Table;
import com.example.demo.repository.OrderReceptionRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderStatusChangeRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerOrderService {

    @Autowired
    private OrderReceptionRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderStatusChangeRepository orderStatusChangeRepository;

    @Transactional
    public OrderPlacementResponseDTO placeOrder(OrderPlacementDTO orderPlacementDTO) {
        // Validate table exists
        Table table = tableRepository.findById(orderPlacementDTO.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found with ID: " + orderPlacementDTO.getTableId()));

        // Create new order
        Order order = new Order();
        order.setTable(table);
        order.setStatus("pending"); // Set default status for new orders
        order.setCreatedAt(OffsetDateTime.now());
        order.setUpdatedAt(OffsetDateTime.now());

        // Calculate total price and create order items
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderPlacementDTO.ItemDTO itemDTO : orderPlacementDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDTO.getId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQty());
            orderItem.setPrice(product.getPrice());

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQty()));
            totalPrice = totalPrice.add(itemTotal);

            orderItems.add(orderItem);
        }

        order.setTotalPrice(totalPrice);

        // Save order first
        Order savedOrder = orderRepository.save(order);

        // Now save each order item to the database
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(savedOrder); // Set the saved order reference
            orderItemRepository.save(orderItem); // Save each order item
        }

        // Record the initial status change
        OrderStatusChange statusChange = new OrderStatusChange();
        statusChange.setOrder(savedOrder);
        statusChange.setStatus("pending");
        statusChange.setCreatedAt(OffsetDateTime.now());
        orderStatusChangeRepository.save(statusChange);

        // Calculate estimated preparation time (simple logic: 5 minutes per item + base 10 minutes)
        int totalItems = orderPlacementDTO.getItems().stream()
                .mapToInt(OrderPlacementDTO.ItemDTO::getQty)
                .sum();
        int estimatedMinutes = 10 + (totalItems * 5);
        String estimatedTime = estimatedMinutes + " minutes";

        return new OrderPlacementResponseDTO(
                savedOrder.getId(),
                estimatedTime,
                "Order placed successfully"
        );
    }

    @Transactional
    public String cancelOrder(Long orderId) {
        // Find the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Check if order can be cancelled (only pending orders can be cancelled)
        String currentStatus = order.getStatus();
        if (!"pending".equalsIgnoreCase(currentStatus)) {
            throw new RuntimeException("Cannot cancel order. Order status is: " + currentStatus +
                    ". Only pending orders can be cancelled.");
        }

        // Update order status to cancelled
        order.setStatus("cancelled");
        order.setUpdatedAt(OffsetDateTime.now());

        // Save the updated order
        orderRepository.save(order);

        // Record the status change
        OrderStatusChange statusChange = new OrderStatusChange();
        statusChange.setOrder(order);
        statusChange.setStatus("cancelled");
        statusChange.setCreatedAt(OffsetDateTime.now());
        orderStatusChangeRepository.save(statusChange);

        return "Order cancelled successfully";
    }
}
