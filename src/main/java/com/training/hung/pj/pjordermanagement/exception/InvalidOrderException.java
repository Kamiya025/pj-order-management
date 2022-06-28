package com.training.hung.pj.pjordermanagement.exception;

import com.training.hung.pj.pjordermanagement.model.Customer;
import com.training.hung.pj.pjordermanagement.model.Order;

import static com.training.hung.pj.pjordermanagement.constant.Constant.INVALID_CUSTOMER;
import static com.training.hung.pj.pjordermanagement.constant.Constant.INVALID_ORDER;

public class InvalidOrderException extends ApplicationException {

    private Order order;

    public InvalidOrderException(Order order, String message) {
        super(message);
        this.order = order;
    }

    @Override
    public String getErrorCode() {
        return INVALID_ORDER;
    }

    public Order getOrder() {
        return this.order;
    }
}
