package com.training.hung.pj.pjordermanagement.service;

import com.training.hung.pj.pjordermanagement.dto.CreateOrderDTO;
import com.training.hung.pj.pjordermanagement.dto.CreateOrderItemDTO;
import com.training.hung.pj.pjordermanagement.dto.QueryOrderDTO;
import com.training.hung.pj.pjordermanagement.dto.RemoveOrderItemDTO;
import com.training.hung.pj.pjordermanagement.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    QueryOrderDTO findById(Long id);
    QueryOrderDTO createOrder(CreateOrderDTO createOrderDTO);
    QueryOrderDTO updateStatusOrder(Long id, OrderStatus status);
    QueryOrderDTO addOrderItem(CreateOrderItemDTO createOrderItemDTO);
    QueryOrderDTO removeOrderItem(RemoveOrderItemDTO removeOrderItemDTO);
    void removeOrder(Long id);
    Page<QueryOrderDTO> findAll(Pageable pageable);
}
