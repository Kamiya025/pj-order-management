package com.training.hung.pj.pjordermanagement.controller;

import com.training.hung.pj.pjordermanagement.dto.CreateCustomerDTO;
import com.training.hung.pj.pjordermanagement.dto.CustomerDTO;
import com.training.hung.pj.pjordermanagement.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController {
    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/")
    public Page<CustomerDTO> findAll()
    {
        log.info("Query all customer default. PageNunber:{}, PageSize: {}", 0, 10);
        return customerService.findAll(PageRequest.of(0,10));
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public Page<CustomerDTO> findbyPage(@PathVariable(name = "pageNumber") Integer pageNumber,
                                        @PathVariable(name = "pageSize") Integer pageSize)
    {
        log.info("Query all customer. PageNunber:{}, PageSize: {}", pageNumber, pageSize);
        return customerService.findAll(PageRequest.of(pageNumber,pageSize));
    }

    @GetMapping("/{id}")
    public CustomerDTO findById(@PathVariable Long id)
    {
        log.info("Query customer has id: {}", id);
        return customerService.findById(id);
    }
    @PutMapping("/")
    public CustomerDTO CreateCustomer(@RequestBody CreateCustomerDTO createCustomerDTO)
    {
        log.info("Create a new customer");
        return customerService.createCustomer(createCustomerDTO);
    }
    @PostMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id,@RequestBody CustomerDTO customerDTO)
    {
        log.info("Update customer has id: {}", id);
        customerDTO.setId(id);
        return customerService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id)
    {
        log.info("Delete customer has id: {}", id);
        customerService.deleteCustomer(id);
        return "Success";
    }
}
