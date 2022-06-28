package com.training.hung.pj.pjordermanagement.exception;

import static com.training.hung.pj.pjordermanagement.constant.Constant.NOT_FOUND_CUSTOMER;
import static com.training.hung.pj.pjordermanagement.constant.Constant.NOT_FOUND_ORDER;

public class OrderNotFoundException extends ApplicationException {
    public OrderNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getErrorCode() {
        return NOT_FOUND_ORDER;
    }
}
