package com.training.hung.pj.pjordermanagement.repository;

import com.training.hung.pj.pjordermanagement.model.Customer;
import com.training.hung.pj.pjordermanagement.model.Order;
import com.training.hung.pj.pjordermanagement.model.OrderStatus;
import com.training.hung.pj.pjordermanagement.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void findById_shouldReturnOrder() {
        Product product1 = Product.builder()
                .name("product")
                .price(100D)
                .avaiable(10L)
                .build();
        entityManager.persist(product1);


        Product productFind = productRepository.findById(product1.getId()).get();
        assertThat(productFind).isEqualTo(product1);

    }

    @Test
    void findById_shouldReturnEmpty() {
        Optional<Product> product = productRepository.findById(1L);
        assertThat(product).isEmpty();
    }

}