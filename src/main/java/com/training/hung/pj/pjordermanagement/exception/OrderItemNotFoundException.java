package com.training.hung.pj.pjordermanagement.exception;

import static com.training.hung.pj.pjordermanagement.constant.Constant.NOT_FOUND_ORDER_ITEM;

public class OrderItemNotFoundException extends ApplicationException {
    public OrderItemNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getErrorCode() {
        return NOT_FOUND_ORDER_ITEM;
    }
}
