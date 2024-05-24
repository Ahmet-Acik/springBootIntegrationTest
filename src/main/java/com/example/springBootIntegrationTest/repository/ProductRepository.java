package com.example.springBootIntegrationTest.repository;

import com.example.springBootIntegrationTest.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findByName(String name);
}
