package com.training.hung.pj.pjordermanagement.exception;

import com.training.hung.pj.pjordermanagement.model.Customer;

import static com.training.hung.pj.pjordermanagement.constant.Constant.INVALID_CUSTOMER;

public class InvalidCustomerException extends ApplicationException {

    private Customer customer;

    public InvalidCustomerException(Customer customer, String message) {
        super(message);
        this.customer = customer;
    }

    @Override
    public String getErrorCode() {
        return INVALID_CUSTOMER;
    }

    public Customer getCustomer() {
        return this.customer;
    }
}
