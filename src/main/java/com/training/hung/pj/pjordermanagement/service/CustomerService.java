package com.training.hung.pj.pjordermanagement.service;

import com.training.hung.pj.pjordermanagement.dto.CreateCustomerDTO;
import com.training.hung.pj.pjordermanagement.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerDTO findById(Long id);
    CustomerDTO createCustomer(CreateCustomerDTO createCustomerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long id);
    Page<CustomerDTO> findAll(Pageable pageable);


}
