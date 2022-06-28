package com.training.hung.pj.pjordermanagement.repository;

import com.training.hung.pj.pjordermanagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
}
