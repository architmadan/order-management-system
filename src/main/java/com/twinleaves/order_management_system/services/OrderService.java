package com.twinleaves.order_management_system.services;

import com.twinleaves.order_management_system.model.Order;
import com.twinleaves.order_management_system.model.OrderItem;
import com.twinleaves.order_management_system.model.Product;
import com.twinleaves.order_management_system.repository.CustomerRepository;
import com.twinleaves.order_management_system.repository.OrderRepository;
import com.twinleaves.order_management_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository; // Add CustomerRepository

    public Order placeOrder(Order order) {
        // Fetch the customer details
        order.setCustomer(customerRepository.findById(order.getCustomer().getId()).orElseThrow(() -> new RuntimeException("Customer not found")));

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            // Fetch the product details
            Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(() -> new RuntimeException("Product not found"));
            item.setProduct(product); // Set the product details in the order item

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    public Map<Long, Long> getTotalOrdersByCustomer() {
                List<Order> orders = orderRepository.findAll();
               Map<Long, Long> totalOrders = new HashMap<>();

                for (Order order : orders) {
                        Long customerId = order.getCustomer().getId();
                        totalOrders.put(customerId, totalOrders.getOrDefault(customerId, 0L) + 1);
                    }

               return totalOrders;
            }

            public List<Map<String, Object>> getTop5Customers() {
                List<Order> orders = orderRepository.findAll();
                Map<Long, Long> totalOrders = new HashMap<>();

                for (Order order : orders) {
                        Long customerId = order.getCustomer().getId();
                        totalOrders.put(customerId, totalOrders.getOrDefault(customerId, 0L) + 1);
                    }

              // Sort customers by the number of orders and get the top 5
              return totalOrders.entrySet().stream()
                      .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sort in descending order
                      .limit(5) // Get top 5
                      .map(entry -> {
                                Map<String, Object> customerData = new HashMap<>();
                                customerData.put("customerId", entry.getKey());
                                customerData.put("orderCount", entry.getValue());
                                return customerData;
                            })
                      .collect(Collectors.toList());
           }
}