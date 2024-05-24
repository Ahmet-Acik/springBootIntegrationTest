package com.example.springBootIntegrationTest;

import com.example.springBootIntegrationTest.entity.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootCrudeExample2AppTests {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost:";
    private static RestTemplate restTemplate;
    @Autowired
    private TestH2Repository testH2Repository;


    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(port + "").concat("/products");
    }

    @Test
    public void productController_TestSaveProduct() {
        Product product = new Product("headset", 2, 299);
        Product responds = restTemplate.postForObject(baseUrl, product, Product.class);
        assertAll(
                () -> assertEquals("headset", responds.getName()),
                () -> assertEquals(2, responds.getQuantity()),
                () -> assertEquals(299, responds.getPrice()),
                () -> assertEquals(1, testH2Repository.findAll().size())
        );
    }

    @Test
    @Sql(statements = "INSERT INTO PRODUCT_TBL (id, name, quantity, price) VALUES (4,'IPhone', 1, 1200)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE from PRODUCT_TBL where name ='IPhone'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void productController_TestFindAllProduct() {
        List<Product> product = restTemplate.getForObject(baseUrl, List.class);
        assertAll(
                () -> assertEquals(1, testH2Repository.findAll().size())
        );
    }

    @Test
    @Sql(statements = "INSERT INTO PRODUCT_TBL (id, name, quantity, price) VALUES (4,'MacBook', 1, 1600)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE from PRODUCT_TBL where id = 4", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void productController_TestFindProductById() {
        Product product = restTemplate.getForObject(baseUrl + "/{id}", Product.class, 4);
        assertAll(
                () -> assertNotNull(product),
                () -> assertTrue(testH2Repository.findById(4).isPresent()),
                () -> assertTrue(testH2Repository.findById(4).get().getName().equals("MacBook"))
        );
    }

    @Test
    @Sql(statements = "INSERT INTO PRODUCT_TBL (id, name, quantity, price) VALUES (3,'Ipad', 1, 1100)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE from PRODUCT_TBL where id = 3", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void productController_TestUpdateProduct() {

        Product product = new Product("Ipad", 1, 1150);
        restTemplate.put(baseUrl + "/update/{id}", product, 3);
        assertAll(
                () -> assertTrue(testH2Repository.findById(3).get().getPrice() == 1150),
                () -> assertTrue(testH2Repository.findById(3).get().getName().equals("Ipad")),
                () -> assertTrue(testH2Repository.findById(3).get().getQuantity() == 1),
                () -> assertEquals(1150, testH2Repository.findById(3).get().getPrice())
        );
    }

    @Test
    @Sql(statements = "INSERT INTO PRODUCT_TBL (id, name, quantity, price) VALUES (5,'Ipod', 1, 350)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void productController_TestDeleteProduct() {
        int size = testH2Repository.findAll().size();
        assertEquals(1, testH2Repository.findAll().size());
        restTemplate.delete(baseUrl + "/delete/{id}", 5);
        assertAll(
                () -> assertTrue(testH2Repository.findAll().size() == 0),
                () -> assertTrue(testH2Repository.findById(5).isEmpty())
        );
    }

}
