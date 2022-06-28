package com.training.hung.pj.pjordermanagement.exception;

import static com.training.hung.pj.pjordermanagement.constant.Constant.NOT_FOUND_CUSTOMER;

public class CustomerNotFoundException extends ApplicationException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getErrorCode() {
        return NOT_FOUND_CUSTOMER;
    }
}
