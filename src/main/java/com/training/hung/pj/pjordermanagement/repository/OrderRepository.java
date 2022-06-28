package com.training.hung.pj.pjordermanagement.repository;

import com.training.hung.pj.pjordermanagement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
}
