package com.training.hung.pj.pjordermanagement.repository;

import com.training.hung.pj.pjordermanagement.model.Customer;
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
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CustomerRepository customerRepository;
    @Test
    public void shouldFindNoCustomers_ifRepositoryIsEmpty() {
        Iterable customers = customerRepository.findAll();
        assertThat(customers).isEmpty();
    }
    @Test
    public void shouldFindAllCustomers() {
        Customer customer1 = Customer.builder()
                                        .name("Customer1")
                                        .mobile("0298462417")
                                        .address("ABC")
                                        .build();
        entityManager.persist(customer1);
        Customer customer2 = Customer.builder()
                .name("Customer2")
                .mobile("0298462413")
                .address("XYZ")
                .build();
        entityManager.persist(customer2);
        Iterable customers = customerRepository.findAll();
        assertThat(customers).hasSize(2).contains(customer1,customer2);
    }
    @Test
    void findByMobile_shouldReturnCustomer() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        entityManager.persist(customer1);
        Customer customer2 = Customer.builder()
                .name("Customer2")
                .mobile("0298462413")
                .address("XYZ")
                .build();
        entityManager.persist(customer2);
        Customer customerFind = customerRepository.findByMobile("0298462413").get();
        assertThat(customerFind).isEqualTo(customer2);

    }

    @Test
    void findByMobile_shouldReturnEmpty() {
        Optional<Customer> customerFind = customerRepository.findByMobile("0298462413");
        assertThat(customerFind).isEmpty();
    }

    @Test
    void findById_shouldReturnCustomer() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        entityManager.persist(customer1);
        Customer customer2 = Customer.builder()
                .name("Customer2")
                .mobile("0298462413")
                .address("XYZ")
                .build();
        entityManager.persist(customer2);
        Customer customerFind = customerRepository.findById(customer1.getId()).get();
        assertThat(customerFind).isEqualTo(customer1);

    }

    @Test
    void findById_shouldReturnEmpty() {
        Optional<Customer> customerFind = customerRepository.findById(1L);
        assertThat(customerFind).isEmpty();
    }
    @Test
    void shouldStoreACustomer() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        Customer customerStore = customerRepository.save(customer1);
        assertThat(customerStore.getId()).isNotZero();
        assertThat(customerStore.getName()).isEqualTo(customer1.getName());
        assertThat(customerStore.getMobile()).isEqualTo(customer1.getMobile());
        assertThat(customerStore.getAddress()).isEqualTo(customer1.getAddress());
    }
    @Test
    void shouldUpdateCustomer() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        entityManager.persist(customer1);
        Customer customerUpdate = customerRepository.findById(customer1.getId()).get();
        customerUpdate.setName("Nguyen Quang Hung");
        customerRepository.save(customerUpdate);

        Customer customerFind = customerRepository.findById(customer1.getId()).get();
        assertThat(customerFind).isEqualTo(customerUpdate);
    }

    @Test
    void shouldDeleteCustomer() {
        Customer customer1 = Customer.builder()
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        entityManager.persist(customer1);
        Customer customer2 = Customer.builder()
                .name("Customer2")
                .mobile("0298462413")
                .address("ABC")
                .build();
        entityManager.persist(customer2);
        customerRepository.delete(customer1);
        Iterable customers = customerRepository.findAll();
        assertThat(customers).hasSize(1).contains(customer2);
    }
}