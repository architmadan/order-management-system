package com.twinleaves.order_management_system.repository;


import com.twinleaves.order_management_system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
