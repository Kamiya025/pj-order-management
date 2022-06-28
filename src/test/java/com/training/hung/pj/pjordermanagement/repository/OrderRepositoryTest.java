package com.training.hung.pj.pjordermanagement.repository;

import com.training.hung.pj.pjordermanagement.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void shouldFindNoOrders_ifRepositoryIsEmpty() {
        Iterable orders = orderRepository.findAll();
        assertThat(orders).isEmpty();
    }
    @Test
    public void shouldFindAllOders() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        entityManager.persist(customer1);
        Order order1 = Order.builder()
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .build();
        entityManager.persist(order1);
        Order order2 = Order.builder()
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .build();
        entityManager.persist(order2);
        Iterable orders = orderRepository.findAll();
        assertThat(orders).hasSize(2).contains(order1,order2);
    }


    @Test
    void findById_shouldReturnOrder() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        entityManager.persist(customer1);
        Order order1 = Order.builder()
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .build();
        entityManager.persist(order1);

        Order orderFind = orderRepository.findById(order1.getId()).get();
        assertThat(orderFind).isEqualTo(order1);

    }

    @Test
    void findById_shouldReturnEmpty() {
        Optional<Order> order = orderRepository.findById(1L);
        assertThat(order).isEmpty();
    }
    @Test
    void shouldStoreAOrder() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        entityManager.persist(customer1);
        Order order1 = Order.builder()
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .build();
        Order OrderStore = orderRepository.save(order1);
        assertThat(OrderStore.getId()).isNotZero();
        assertThat(OrderStore.getStatus()).isEqualTo(order1.getStatus());
        assertThat(OrderStore.getCustomer()).isEqualTo(customer1);
    }
    @Test
    void shouldUpdateOrder() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        entityManager.persist(customer1);
        Order order1 = Order.builder()
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .build();
        entityManager.persist(order1);
        Order orderUpdate = orderRepository.findById(order1.getId()).get();
        orderUpdate.setStatus(OrderStatus.PAID);
        orderRepository.save(orderUpdate);

        Order orderFind = orderRepository.findById(order1.getId()).get();
        assertThat(orderFind).isEqualTo(orderUpdate);
    }

    @Test
    void shouldDeleteOrder() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        entityManager.persist(customer1);
        Order order1 = Order.builder()
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .build();
        entityManager.persist(order1);
        Order order2 = Order.builder()
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .build();
        entityManager.persist(order2);
        orderRepository.delete(order1);
        Iterable orders = orderRepository.findAll();
        assertThat(orders).hasSize(1).contains(order2);
    }
    @Test
    void shouldOrderAddOrderItem() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        entityManager.persist(customer1);
        Product product1 = Product.builder()
                .name("product")
                .price(100D)
                .avaiable(10L)
                .build();
        Order order1 = Order.builder()
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .build();
        entityManager.persist(order1);
        OrderItem orderItem1 = OrderItem.builder()
                .product(product1)
                .order(order1)
                .quantity(1)
                .amount(100D)
                .build();
        order1.setOrderItems(new ArrayList<>());
        order1.getOrderItems().add(orderItem1);
        orderRepository.save(order1);

        Order orderFind = orderRepository.findById(order1.getId()).get();
        assertThat(orderFind.getOrderItems().size()).isEqualTo(1);
        assertThat(orderFind.getOrderItems().get(0).getOrder()).isEqualTo(orderFind);
    }

    @Test
    void shouldDeleteCustomer() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        entityManager.persist(customer1);
        Order order1 = Order.builder()
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .build();
        entityManager.persist(order1);
        Order order2 = Order.builder()
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .build();
        entityManager.persist(order2);
        orderRepository.delete(order1);
        Iterable orders = orderRepository.findAll();
        assertThat(orders).hasSize(1).contains(order2);
    }

}