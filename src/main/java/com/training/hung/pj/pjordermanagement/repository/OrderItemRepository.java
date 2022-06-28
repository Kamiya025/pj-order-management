package com.training.hung.pj.pjordermanagement.repository;

import com.training.hung.pj.pjordermanagement.model.Customer;
import com.training.hung.pj.pjordermanagement.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
