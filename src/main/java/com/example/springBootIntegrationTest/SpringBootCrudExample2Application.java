package com.example.springBootIntegrationTest;

import com.example.springBootIntegrationTest.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringBootCrudExample2Application {

    @Autowired
    private ProductRepository repository;

    @PostConstruct
    public void init() {
//        repository.saveAll(
//                Stream
//                        .of(
//                                new Product("Book", 1, 299),
//                                new Product("mobile", 1, 39999))
//                        .collect(Collectors.toList())
//        );
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCrudExample2Application.class, args);
    }

}