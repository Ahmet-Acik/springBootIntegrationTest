package com.example.springBootIntegrationTest;

import com.example.springBootIntegrationTest.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestH2Repository extends JpaRepository<Product, Integer> {

}
