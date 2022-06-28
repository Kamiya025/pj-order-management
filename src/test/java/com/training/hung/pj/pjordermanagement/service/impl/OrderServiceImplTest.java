package com.training.hung.pj.pjordermanagement.service.impl;

import com.training.hung.pj.pjordermanagement.dto.*;
import com.training.hung.pj.pjordermanagement.exception.CustomerNotFoundException;
import com.training.hung.pj.pjordermanagement.exception.InvalidOrderException;
import com.training.hung.pj.pjordermanagement.exception.OrderItemNotFoundException;
import com.training.hung.pj.pjordermanagement.exception.OrderNotFoundException;
import com.training.hung.pj.pjordermanagement.model.*;
import com.training.hung.pj.pjordermanagement.repository.CustomerRepository;
import com.training.hung.pj.pjordermanagement.repository.OrderItemRepository;
import com.training.hung.pj.pjordermanagement.repository.OrderRepository;
import com.training.hung.pj.pjordermanagement.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void whenFindById_shouldReturnQueryOderDTO() {

        Order order1 = Order.builder()
                .id(1L)
                .customer(new Customer())
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));
        assertThat(orderService.findById(1L)).isEqualTo(QueryOrderDTO.Mapper.fromEntity(order1));
        verify(orderRepository,times(1)).findById(1L);
    }

    @Test
    void whenFindById_shouldReturnException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.findById(1L))
                .isInstanceOf(OrderNotFoundException.class);
        verify(orderRepository,times(1)).findById(1L);
    }

    @Test
    void findAll() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        Order order1 = Order.builder()
                .id(1L)
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();
        Pageable pageable = PageRequest.of(0,1);
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        Page<Order> orders = new PageImpl<>(orderList);
        Page<QueryOrderDTO> queryOrderDTOS = new PageImpl<>(orderList.stream()
                                        .map(QueryOrderDTO.Mapper::fromEntity).toList());
        when(orderRepository.findAll(pageable)).thenReturn(orders);
        assertThat(orderService.findAll(pageable)).isEqualTo(queryOrderDTOS);
        verify(orderRepository,times(1)).findAll(pageable);
    }

    @Test
    void whenCreateOrder_shouldReturnQueryOrderDTO() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        Order order1 = Order.builder()
                .id(1L)
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer1));
        when(orderRepository.save(any())).thenReturn(order1);
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setCustomerId(1L);
        createOrderDTO.setOrderItems(new ArrayList<>());
        QueryOrderDTO queryOrderDTOS = orderService.createOrder(createOrderDTO);
        assertThat(queryOrderDTOS).isEqualTo(QueryOrderDTO.Mapper.fromEntity(order1));
        verify(orderRepository,times(1)).save(any());
    }

    @Test
    void whenCreateOrderIfCustomerNotFound_shouldReturnExeption() {

        when(customerRepository.findById(any())).thenReturn(Optional.empty());
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setCustomerId(1L);
        createOrderDTO.setOrderItems(new ArrayList<>());
        assertThatThrownBy(() -> orderService.createOrder(createOrderDTO))
                .isInstanceOf(CustomerNotFoundException.class);
        verify(customerRepository,times(1)).findById(any());
        verify(orderRepository,times(0)).save(any());
    }
    @Test
    void whenCreateOrderIfProductNotFound_shouldReturnExeption() {

        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setCustomerId(1L);
        createOrderDTO.setOrderItems(new ArrayList<>());
        createOrderDTO.getOrderItems().add(CreateOrderItemDTO.builder().productId(1L).build());
        assertThatThrownBy(() -> orderService.createOrder(createOrderDTO))
                .isInstanceOf(InvalidOrderException.class);
        verify(customerRepository,times(1)).findById(any());
        verify(productRepository,times(1)).findById(any());
        verify(orderRepository,times(0)).save(any());
    }

    @Test
    void whenCreateOrderIfProductNotAvailable_shouldReturnExeption() {

        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
        when(productRepository.findById(any())).thenReturn(Optional.of(Product.builder()
                                                                            .avaiable(0L)
                                                                            .name("product1")
                                                                            .price(1000D)
                                                                            .id(1L)
                                                                            .build()));
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setCustomerId(1L);
        createOrderDTO.setOrderItems(new ArrayList<>());
        createOrderDTO.getOrderItems().add(CreateOrderItemDTO.builder().quantity(10).productId(1L).build());
        assertThatThrownBy(() -> orderService.createOrder(createOrderDTO))
                .isInstanceOf(InvalidOrderException.class);
        verify(customerRepository,times(1)).findById(any());
        verify(productRepository,times(1)).findById(any());
        verify(orderRepository,times(0)).save(any());
    }

    @Test
    void whenUpdateStatusOrder_shouldReturnQueryOrderDTO() {
        Order order1 = Order.builder()
                .id(1L)
                .customer(new Customer())
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();
        Order order2 = Order.builder()
                .id(1L)
                .customer(new Customer())
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.PAID)
                .orderItems(new ArrayList<>())
                .build();
        when(orderRepository.findById(any())).thenReturn(Optional.of(order1));
        when(orderRepository.save(any())).thenReturn(order1);

        QueryOrderDTO queryOrderDTO = orderService.updateStatusOrder(1L,OrderStatus.PAID);
        assertThat(queryOrderDTO).isEqualTo(QueryOrderDTO.Mapper.fromEntity(order2));
        verify(orderRepository,times(1)).findById(any());
        verify(orderRepository,times(1)).save(any());
    }
    @Test
    void whenUpdateStatusOrder_shouldReturnException() {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(()->orderService.updateStatusOrder(1L,OrderStatus.PAID))
                .isInstanceOf(OrderNotFoundException.class);
        verify(orderRepository,times(1)).findById(any());
        verify(orderRepository,times(0)).save(any());
    }

    @Test
    void whenAddOrderItem_shouldReturnQueryOrderDTO() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        Order order1 = Order.builder()
                .id(1L)
                .customer(customer1)
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();
        Product product1 = Product.builder()
                .avaiable(10L)
                .name("product1")
                .price(1000D)
                .id(1L)
                .build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product1));
        when(orderRepository.findById(any())).thenReturn(Optional.of(order1));
        when(orderRepository.save(any())).thenReturn(order1);
        CreateOrderItemDTO createOrderItemDTO = CreateOrderItemDTO.builder()
                .quantity(1)
                .productId(1L)
                .orderId(1L)
                .build();
        QueryOrderDTO queryOrderDTO = orderService.addOrderItem(createOrderItemDTO);
        assertThat(queryOrderDTO.getOrderItems().size()).isEqualTo(1);
        verify(orderRepository,times(1)).findById(any());
        verify(orderRepository,times(1)).save(any());
    }

    @Test
    void whenAddOrderItemIfOrderNotFound_shouldReturnException() {
        Order order1 = Order.builder()
                .id(1L)
                .customer(new Customer())
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();
       Product product1 = Product.builder()
                .avaiable(10L)
                .name("product1")
                .price(1000D)
                .id(1L)
                .build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product1));
        when(orderRepository.findById(any())).thenReturn(Optional.empty());
        when(orderRepository.save(any())).thenReturn(order1);
        CreateOrderItemDTO createOrderItemDTO = CreateOrderItemDTO.builder()
                .quantity(1)
                .productId(1L)
                .orderId(1L)
                .build();
        assertThatThrownBy(() -> orderService.addOrderItem(createOrderItemDTO))
                .isInstanceOf(OrderNotFoundException.class);

        verify(orderRepository,times(1)).findById(any());
        verify(orderRepository,times(0)).save(any());
    }

    @Test
    void whenAddOrderItemIfProductNotFound_shouldReturnException() {
        Order order1 = Order.builder()
                .id(1L)
                .customer(new Customer())
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();

        when(productRepository.findById(any())).thenReturn(Optional.empty());
        when(orderRepository.findById(any())).thenReturn(Optional.of(order1));
        when(orderRepository.save(any())).thenReturn(order1);
        CreateOrderItemDTO createOrderItemDTO = CreateOrderItemDTO.builder()
                .quantity(1)
                .productId(1L)
                .orderId(1L)
                .build();
        assertThatThrownBy(() -> orderService.addOrderItem(createOrderItemDTO))
                .isInstanceOf(InvalidOrderException.class);

        verify(orderRepository,times(1)).findById(any());
        verify(productRepository,times(1)).findById(any());
        verify(orderRepository,times(0)).save(any());
    }

    @Test
    void whenAddOrderItemIfProductNotAvaiable_shouldReturnException() {
        Order order1 = Order.builder()
                .id(1L)
                .customer(new Customer())
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();
        Product product1 = Product.builder()
                .avaiable(0L)
                .name("product1")
                .price(1000D)
                .id(1L)
                .build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product1));
        when(orderRepository.findById(any())).thenReturn(Optional.of(order1));
        when(orderRepository.save(any())).thenReturn(order1);
        CreateOrderItemDTO createOrderItemDTO = CreateOrderItemDTO.builder()
                .quantity(1)
                .productId(1L)
                .orderId(1L)
                .build();
        assertThatThrownBy(() -> orderService.addOrderItem(createOrderItemDTO))
                .isInstanceOf(InvalidOrderException.class);

        verify(orderRepository,times(1)).findById(any());
        verify(productRepository,times(1)).findById(any());
        verify(orderRepository,times(0)).save(any());
    }

    @Test
    void whenRemoveOrderItem_shouldReturnQueryOrderDTO() {
        Order order1 = Order.builder()
                .id(1L)
                .customer(new Customer())
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();
        OrderItem orderItem1 = OrderItem.builder().id(1L).order(order1).build();
        order1.getOrderItems().add(orderItem1);
        when(orderItemRepository.findById(any())).thenReturn(Optional.of(orderItem1));
        when(orderRepository.findById(any())).thenReturn(Optional.of(order1));
        when(orderRepository.save(any())).thenReturn(order1);

        QueryOrderDTO queryOrderDTO = orderService.removeOrderItem(RemoveOrderItemDTO.builder()
                        .orderId(1L)
                        .orderId(1L)
                .build());
        assertThat(queryOrderDTO.getOrderItems().size()).isEqualTo(0);
        verify(orderRepository,times(1)).findById(any());
        verify(orderItemRepository,times(1)).findById(any());
        verify(orderRepository,times(1)).save(any());
    }

    @Test
    void whenRemoveOrderItemIfOrderNotFound_shouldReturnException() {

        OrderItem orderItem1 = OrderItem.builder().id(1L).order(new Order()).build();
        when(orderItemRepository.findById(any())).thenReturn(Optional.of(orderItem1));
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(()->orderService.removeOrderItem(RemoveOrderItemDTO.builder()
                .orderId(1L)
                .orderId(1L)
                .build())).isInstanceOf(OrderNotFoundException.class);
        verify(orderRepository,times(1)).findById(any());
        verify(orderItemRepository,times(0)).findById(any());
        verify(orderRepository,times(0)).save(any());
    }

    @Test
    void whenRemoveOrderItemIfOrderItemNotFound_shouldReturnException() {
        Order order1 = Order.builder()
                .id(1L)
                .customer(new Customer())
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();

        when(orderItemRepository.findById(any())).thenReturn(Optional.empty());
        when(orderRepository.findById(any())).thenReturn(Optional.of(order1));
        when(orderRepository.save(any())).thenReturn(order1);

        assertThatThrownBy(() -> orderService.removeOrderItem(RemoveOrderItemDTO.builder()
                .orderId(1L)
                .orderId(1L)
                .build())).isInstanceOf(OrderItemNotFoundException.class);
        verify(orderRepository,times(1)).findById(any());
        verify(orderItemRepository,times(1)).findById(any());
        verify(orderRepository,times(0)).save(any());
    }

    @Test
    void whenDeleteOrder_shouldSuccess() {
        Order order1 = Order.builder()
                .id(1L)
                .customer(new Customer())
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();
        when(orderRepository.findById(any())).thenReturn(Optional.of(order1));
        orderService.removeOrder(1L);
        verify(orderRepository,times(1)).findById(any());
        verify(orderRepository,times(1)).delete(any());
    }
    @Test
    void whenDeleteOrder_shouldReturnException() {

        when(orderRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.removeOrder(1L))
                .isInstanceOf(OrderNotFoundException.class);
        verify(orderRepository,times(1)).findById(any());
        verify(orderRepository,times(0)).delete(any());
    }



}