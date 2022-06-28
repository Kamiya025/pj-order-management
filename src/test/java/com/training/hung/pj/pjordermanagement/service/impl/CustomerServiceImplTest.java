package com.training.hung.pj.pjordermanagement.service.impl;

import com.training.hung.pj.pjordermanagement.dto.CreateCustomerDTO;
import com.training.hung.pj.pjordermanagement.dto.CustomerDTO;
import com.training.hung.pj.pjordermanagement.exception.CustomerNotFoundException;
import com.training.hung.pj.pjordermanagement.exception.InvalidCustomerException;
import com.training.hung.pj.pjordermanagement.model.Customer;
import com.training.hung.pj.pjordermanagement.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;
    @Test
    void whenFindById_shouldReturnCustomerDTO() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0298462417")
                .address("ABC")
                .build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        assertThat(customerService.findById(1L)).isEqualTo(CustomerDTO.Mapper.fromEntity(customer1));
        verify(customerRepository,times(1)).findById(1L);

    }
    @Test
    void whenFindById_shouldReturnException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> customerService.findById(1L))
                .isInstanceOf(CustomerNotFoundException.class);
        verify(customerRepository,times(1)).findById(1L);
    }

    @Test
    void whenCreateCustomer_shouldReturnCustomerDTO() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        when(customerRepository.save(any())).thenReturn(customer1);
        CreateCustomerDTO createCustomerDTO= new CreateCustomerDTO();
        createCustomerDTO.setName("Customer1");
        createCustomerDTO.setMobile("0123456789");
        createCustomerDTO.setAddress("ABC");
        CustomerDTO customerDTOCreate = customerService.createCustomer(createCustomerDTO);
        assertThat(customerDTOCreate).isEqualTo(CustomerDTO.Mapper.fromEntity(customer1));
        verify(customerRepository,times(1)).save(any());
    }

    @Test
    void whenCreateCustomerIfNameIsBlank_shouldReturnException() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        when(customerRepository.save(any())).thenReturn(customer1);
        CreateCustomerDTO createCustomerDTO= new CreateCustomerDTO();
        createCustomerDTO.setName("");
        createCustomerDTO.setMobile("0123456789");
        createCustomerDTO.setAddress("ABC");
        assertThatThrownBy(() -> customerService.createCustomer(createCustomerDTO))
                .isInstanceOf(InvalidCustomerException.class);
        verify(customerRepository,times(0)).save(any());
    }
    @Test
    void whenCreateCustomerIfAddressIsBlank_shouldReturnException() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        when(customerRepository.save(any())).thenReturn(customer1);
        CreateCustomerDTO createCustomerDTO= new CreateCustomerDTO();
        createCustomerDTO.setName("Customer1");
        createCustomerDTO.setMobile("0123456789");
        createCustomerDTO.setAddress("");
        assertThatThrownBy(() -> customerService.createCustomer(createCustomerDTO))
                .isInstanceOf(InvalidCustomerException.class);
        verify(customerRepository,times(0)).save(any());
    }

    @Test
    void whenCreateCustomerIfMobileIsExist_shouldReturnException() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        when(customerRepository.save(any())).thenReturn(customer1);
        when(customerRepository.findByMobile(any())).thenReturn(Optional.of(customer1));
        CreateCustomerDTO createCustomerDTO= new CreateCustomerDTO();
        createCustomerDTO.setName("Customer1");
        createCustomerDTO.setMobile("0123456789");
        createCustomerDTO.setAddress("HD");
        assertThatThrownBy(() -> customerService.createCustomer(createCustomerDTO))
                .isInstanceOf(InvalidCustomerException.class);
        verify(customerRepository,times(1)).findByMobile(any());
        verify(customerRepository,times(0)).save(any());
    }
    @Test
    void whenCreateCustomerIfMobileInvalid_shouldReturnException() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        when(customerRepository.save(any())).thenReturn(customer1);
        when(customerRepository.findByMobile(any())).thenReturn(Optional.of(customer1));
        CreateCustomerDTO createCustomerDTO= new CreateCustomerDTO();
        createCustomerDTO.setName("Customer1");
        createCustomerDTO.setMobile("012");
        createCustomerDTO.setAddress("HD");
        assertThatThrownBy(() -> customerService.createCustomer(createCustomerDTO))
                .isInstanceOf(InvalidCustomerException.class);
        verify(customerRepository,times(0)).findByMobile(any());
        verify(customerRepository,times(0)).save(any());
    }


    @Test
    void whenUpdateCustomer_shouldReturnCustomerDTO() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer1));
        when(customerRepository.save(any())).thenReturn(customer1);
        CustomerDTO customerDTO= new CustomerDTO();
        customerDTO.setMobile("0123456789");
        customerDTO.setAddress("ABCD");
        CustomerDTO customerDTOUpdate = customerService.updateCustomer(customerDTO);
        assertThat(customerDTOUpdate).isEqualTo(CustomerDTO.Mapper.fromEntity(customer1));
        verify(customerRepository,times(1)).findById(any());
        verify(customerRepository,times(1)).save(any());
    }


    @Test
    void whenUpdateCustomerIfMobileIsExist_shouldReturnException() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        Customer customer2 = Customer.builder()
                .id(2L)
                .name("Customer2")
                .mobile("0123456780")
                .address("ABC")
                .build();
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer1));
        when(customerRepository.save(any())).thenReturn(customer1);
        when(customerRepository.findByMobile("0123456780")).thenReturn(Optional.of(customer2));
        CustomerDTO customerDTO= new CustomerDTO();
        customerDTO.setMobile("0123456780");
        customerDTO.setAddress("HD");
        assertThatThrownBy(() -> customerService.updateCustomer(customerDTO))
                .isInstanceOf(InvalidCustomerException.class);
        verify(customerRepository,times(1)).findById(any());
        verify(customerRepository,times(1)).findByMobile("0123456780");
        verify(customerRepository,times(0)).save(any());
    }
    @Test
    void whenUpdateCustomerIfMobileInvalid_shouldReturnException() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer1));
        when(customerRepository.save(any())).thenReturn(customer1);
        when(customerRepository.findByMobile(any())).thenReturn(Optional.of(customer1));
        CustomerDTO customerDTO= new CustomerDTO();
        customerDTO.setMobile("012");
        customerDTO.setAddress("HD");
        assertThatThrownBy(() -> customerService.updateCustomer(customerDTO))
                .isInstanceOf(InvalidCustomerException.class);
        verify(customerRepository,times(1)).findById(any());
        verify(customerRepository,times(0)).findByMobile(any());
        verify(customerRepository,times(0)).save(any());
    }

    @Test
    void whenDeleteCustomer_shouldSuccess() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer1));
        customerService.deleteCustomer(1L);
        verify(customerRepository,times(1)).findById(any());
    }
    @Test
    void whenDeleteCustomer_shouldReturnException() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        when(customerRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> customerService.deleteCustomer(1L))
                .isInstanceOf(CustomerNotFoundException.class);
        verify(customerRepository,times(1)).findById(any());
    }

    @Test
    void findAll() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .mobile("0123456789")
                .address("ABC")
                .build();
        Pageable pageable = PageRequest.of(0,1);
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer1);
        Page<Customer> customers = new PageImpl<Customer>(customerList);
        Page<CustomerDTO> customerDTOs = new PageImpl<CustomerDTO>(customerList.stream()
                                                                    .map(CustomerDTO.Mapper::fromEntity).toList());
        when(customerRepository.findAll(pageable)).thenReturn(customers);
        assertThat(customerService.findAll(pageable)).isEqualTo(customerDTOs);
        verify(customerRepository,times(1)).findAll(pageable);

    }
}