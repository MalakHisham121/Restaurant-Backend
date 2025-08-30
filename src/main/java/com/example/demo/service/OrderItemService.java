package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.OrderItemRepo;
import com.example.demo.repository.OrderRepo;
import com.example.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    private final OrderItemRepo orderItemRepo;
    private final OrderRepo orderRepo;

    private final ProductRepository productRepo;
    // eyJhbGciOiJIUzI1NiJ9.e30.m_Hw9T5ursrEiDKbqw1NM5qorV0qx77edIEJp5YINtE
    public OrderItemService(OrderItemRepo orderItemRepo,OrderRepo orderRepo,ProductRepository productRepo){
        this.orderItemRepo = orderItemRepo;
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;

    }


    @Transactional
    public List<OrderItem> findByOrderId(Long orderId){

        return orderItemRepo.findByOrderId(orderId);
    }
    // this is one of admin function
    @Transactional
    public Order addOrderItem(OrderItemDTO orderItem){

        if(orderItem.getOrderId() == null|| orderItem.getProductId()==null|| orderItem.getQuantity()==null|| orderItem.getPrice() == null) {
            throw new IllegalArgumentException("Your passed order item is not valid , product id ,order id, quantity and price connot be null ");
        }

            Order order = orderRepo.findById(orderItem.getOrderId()). orElseThrow(()->new RuntimeException("Order not found with ID "+ orderItem.getOrderId()));

        OrderItem item = new OrderItem();
        item.setOrder(order);
        Product product =  productRepo.findById(orderItem.getProductId()).orElseThrow(()->new RuntimeException("Product not found"));
            item.setProduct(product);
            item.setQuantity(orderItem.getQuantity());
            item.setPrice(calcPrice(orderItem.getQuantity(),orderItem.getPrice()));


        orderItemRepo.save(item);

        order.setTotalPrice(order.getTotalPrice().add(item.getPrice()));
        orderRepo.save(order);

        return order;

    }
    private BigDecimal calcPrice(int quantity, BigDecimal p) {
        return p.multiply(BigDecimal.valueOf(quantity));
    }

    private void calcTotalPrice(Order order, int quantity , BigDecimal Unitprice ){
        BigDecimal total = Unitprice.multiply( BigDecimal.valueOf(quantity));
        order.setTotalPrice(order.getTotalPrice().add(total));
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
    // this function (mapToOrderDTO) has problems
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

    @Transactional
    public OrderItem updateOrderItemQuantity(Long orderItemId,int newQuantity){

        if(orderItemId==null) {
            throw new IllegalArgumentException("Order id connot be null ");
        }
        OrderItem ActualorderItem = orderItemRepo.findById(orderItemId). orElseThrow(()->new RuntimeException("Order Item not found with ID "+ orderItemId));
        int avaliable = ActualorderItem.getProduct().getInStockQuantity()+ ActualorderItem.getQuantity();
        Order order = ActualorderItem.getOrder();
        BigDecimal unitPrice =ActualorderItem.getProduct().getPrice();
        if(avaliable - newQuantity >=0){
          calcTotalPrice(order,avaliable - newQuantity ,unitPrice);
            ActualorderItem.getProduct().setInStockQuantity(avaliable - newQuantity );

            ActualorderItem.setQuantity(newQuantity);
            ActualorderItem.setPrice(calcPrice(newQuantity,unitPrice));
        }
        else{
            throw new IllegalArgumentException("Only the available quantity of the "+ActualorderItem.getProduct().getName() +"Product is : "+ avaliable );
        }



        productRepo.save(ActualorderItem.getProduct());
        orderItemRepo.save(ActualorderItem);



        return ActualorderItem;

    }

    @Transactional
    public String deleteOrderItem(Long orderItemId){
        orderItemRepo.delete(orderItemRepo.findById(orderItemId).orElseThrow(()->new RuntimeException("No such order item with given ID!")));

        return "Order item deleted succesfully";

    }



}
