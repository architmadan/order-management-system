package com.twinleaves.order_management_system.controller;


import com.twinleaves.order_management_system.model.Order;
import com.twinleaves.order_management_system.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        Order savedOrder = orderService.placeOrder(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable Long customerId) {
        return new ResponseEntity<>(orderService.getOrdersByCustomerId(customerId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    //  total orders by each customer
    @GetMapping("/total-orders")
    public ResponseEntity<Map<Long, Long>> getTotalOrdersByCustomer() {
        Map<Long, Long> totalOrders = orderService.getTotalOrdersByCustomer();
        return new ResponseEntity<>(totalOrders, HttpStatus.OK);
    }

    // top 5 customers
    @GetMapping("/top-customers")
    public ResponseEntity<List<Map<String, Object>>> getTop5Customers() {
        List<Map<String, Object>> topCustomers = orderService.getTop5Customers();
        return new ResponseEntity<>(topCustomers, HttpStatus.OK);
    }
}