package com.training.hung.pj.pjordermanagement.service.impl;

import com.training.hung.pj.pjordermanagement.dto.CreateOrderDTO;
import com.training.hung.pj.pjordermanagement.dto.CreateOrderItemDTO;
import com.training.hung.pj.pjordermanagement.dto.QueryOrderDTO;
import com.training.hung.pj.pjordermanagement.dto.RemoveOrderItemDTO;
import com.training.hung.pj.pjordermanagement.exception.CustomerNotFoundException;
import com.training.hung.pj.pjordermanagement.exception.InvalidOrderException;
import com.training.hung.pj.pjordermanagement.exception.OrderItemNotFoundException;
import com.training.hung.pj.pjordermanagement.exception.OrderNotFoundException;
import com.training.hung.pj.pjordermanagement.model.*;
import com.training.hung.pj.pjordermanagement.repository.CustomerRepository;
import com.training.hung.pj.pjordermanagement.repository.OrderItemRepository;
import com.training.hung.pj.pjordermanagement.repository.OrderRepository;
import com.training.hung.pj.pjordermanagement.repository.ProductRepository;
import com.training.hung.pj.pjordermanagement.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private OrderItemRepository orderItemRepository;
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            CustomerRepository customerRepository,
                            ProductRepository productRepository,
                            OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public QueryOrderDTO findById(Long id) {
        return QueryOrderDTO.Mapper.fromEntity(orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found")));
    }

    @Override
    @Transactional
    public QueryOrderDTO createOrder(CreateOrderDTO createOrderDTO) {
        Order order = new Order();
        Customer customer = customerRepository.findById(createOrderDTO.getCustomerId())
                                .orElseThrow(() -> new CustomerNotFoundException("Not found customer"));
        order.setCustomer(customer);
        order.setStatus(OrderStatus.CREATED);

        List<OrderItem> orderItems = new ArrayList<>();
        for(CreateOrderItemDTO cOrderItemDTO: createOrderDTO.getOrderItems())
        {
            orderItems.add(createNewOrderItem(order,cOrderItemDTO));
        }
        order.setOrderItems(orderItems);
        order.setTotalAmount(orderItems.stream().mapToDouble(item ->item.getAmount()).sum());
        order.setOrderDateTime(LocalDateTime.now());
        return QueryOrderDTO.Mapper.fromEntity(orderRepository.save(order));
    }

    @Override
    public QueryOrderDTO updateStatusOrder(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(status);
        return QueryOrderDTO.Mapper.fromEntity(orderRepository.save(order));
    }

    @Override
    public QueryOrderDTO addOrderItem(CreateOrderItemDTO createOrderItemDTO) {
        Order order = orderRepository.findById(createOrderItemDTO.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if(!order.getStatus().equals(OrderStatus.CREATED))
            throw new InvalidOrderException(order,"Order status must Created");
        order.getOrderItems().add(createNewOrderItem(order, createOrderItemDTO));
        order.setTotalAmount(order.getOrderItems().stream().mapToDouble(item ->item.getAmount()).sum());
        return QueryOrderDTO.Mapper.fromEntity(orderRepository.save(order));
    }

    private OrderItem createNewOrderItem(Order order,CreateOrderItemDTO createOrderItemDTO)
    {
        Product product = productRepository.findById(createOrderItemDTO.getProductId())
                .orElseThrow(() -> new InvalidOrderException(order,"Not found product id =" + createOrderItemDTO.getProductId()));
        Long newAvailable = product.getAvaiable()-createOrderItemDTO.getQuantity();
        if(newAvailable<0) throw new InvalidOrderException(order,"Product not Available");
        product.setAvaiable(newAvailable);
        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(createOrderItemDTO.getQuantity())
                .amount(product.getPrice()*createOrderItemDTO.getQuantity())
                .build();


    }

    @Override
    public QueryOrderDTO removeOrderItem(RemoveOrderItemDTO removeOrderItemDTO) {
        Order order = orderRepository.findById(removeOrderItemDTO.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if(!order.getStatus().equals(OrderStatus.CREATED))
            throw new InvalidOrderException(order,"Order status must Created");
        OrderItem orderItem = orderItemRepository.findById(removeOrderItemDTO.getOrderItemId())
                .orElseThrow(() -> new OrderItemNotFoundException("Order item not found"));
        order.getOrderItems().remove(orderItem);
        order.setTotalAmount(order.getOrderItems().stream().mapToDouble(item ->item.getAmount()).sum());
        return QueryOrderDTO.Mapper.fromEntity(orderRepository.save(order));
    }

    @Override
    public void removeOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if(order.getStatus().equals(OrderStatus.PAID))
            throw new InvalidOrderException(order, "Order status must Created or cancalled");
        orderRepository.delete(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QueryOrderDTO> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(QueryOrderDTO.Mapper::fromEntity);
    }
}
