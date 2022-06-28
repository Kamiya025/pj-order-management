package com.training.hung.pj.pjordermanagement.service.impl;

import com.training.hung.pj.pjordermanagement.dto.CreateCustomerDTO;
import com.training.hung.pj.pjordermanagement.dto.CustomerDTO;
import com.training.hung.pj.pjordermanagement.exception.CustomerNotFoundException;
import com.training.hung.pj.pjordermanagement.exception.InvalidCustomerException;
import com.training.hung.pj.pjordermanagement.model.Customer;
import com.training.hung.pj.pjordermanagement.repository.CustomerRepository;
import com.training.hung.pj.pjordermanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerServiceImpl implements CustomerService {
    CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO findById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Not found customer"));
        return CustomerDTO.Mapper.fromEntity(customer);
    }

    @Override
    public CustomerDTO createCustomer(CreateCustomerDTO createCustomerDTO) {
        Customer customer = Customer.builder()
                .name(createCustomerDTO.getName())
                .mobile(createCustomerDTO.getMobile())
                .address(createCustomerDTO.getAddress())
                .build();
        valid(customer);
        Optional<Customer> optionalCustomer = customerRepository.findByMobile(customer.getMobile());
        if(!optionalCustomer.isEmpty())
            throw new InvalidCustomerException(customer, "Input mobile registered");
        return CustomerDTO.Mapper.fromEntity(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(customerDTO.getId())
                                .orElseThrow(() -> new CustomerNotFoundException("Not found customer"));
        customer.setMobile(customerDTO.getMobile());
        customer.setAddress(customerDTO.getAddress());
        valid(customer);
        Optional<Customer> optionalCustomer = customerRepository.findByMobile(customer.getMobile());
        if(!optionalCustomer.isEmpty()) {
            if(!optionalCustomer.get().equals(customer))
            throw new InvalidCustomerException(customer, "Input mobile registered");
        }
        return CustomerDTO.Mapper.fromEntity(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Not found customer"));
        customerRepository.delete(customer);
    }

    private void valid(Customer customer) {
        if(customer.getName().isBlank()||customer.getName().length()>=100)
            throw new InvalidCustomerException(customer, "Input name can't blank or length > 100 char");
        if(customer.getAddress().isBlank()||customer.getAddress().length()>=100)
            throw new InvalidCustomerException(customer, "Input address can't blank or length >100 char");
        Pattern patternMobile = Pattern.compile("0[0-9]{9}", Pattern.CASE_INSENSITIVE);
        Matcher matcherMobile = patternMobile.matcher(customer.getMobile());
        if (!matcherMobile.find()) throw new InvalidCustomerException(customer, "Input mobile invalid");

    }

    @Override
    public Page<CustomerDTO> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable).map(CustomerDTO.Mapper::fromEntity);
    }
}
