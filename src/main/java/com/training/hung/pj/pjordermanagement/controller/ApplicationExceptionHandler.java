package com.training.hung.pj.pjordermanagement.controller;

import com.training.hung.pj.pjordermanagement.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.training.hung.pj.pjordermanagement.constant.Constant.*;


@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    @ExceptionHandler(value = {
            OrderNotFoundException.class
    })
    protected ResponseEntity<ErrorMessage> handleOrderNotFoundException(Exception exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.builder().code(NOT_FOUND_ORDER).message(exception.getMessage()).build());
    }
    @ExceptionHandler(value = {
            OrderItemNotFoundException.class
    })
    protected ResponseEntity<ErrorMessage> handleOrderItemNotFoundException(Exception exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.builder().code(NOT_FOUND_ORDER_ITEM).message(exception.getMessage()).build());
    }
    @ExceptionHandler(value = {
            InvalidOrderException.class
    })
    protected ResponseEntity<ErrorMessage> handleInvalidOrderException(Exception exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessage.builder().code(INVALID_ORDER).message(exception.getMessage()).build());
    }
    @ExceptionHandler(value = {
            CustomerNotFoundException.class
    })
    protected ResponseEntity<ErrorMessage> handleCustomerNotFoundException(Exception exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.builder().code(NOT_FOUND_CUSTOMER).message(exception.getMessage()).build());
    }
    @ExceptionHandler(value = {
            InvalidCustomerException.class
    })
    protected ResponseEntity<ErrorMessage> handleInvalidCustomerException(Exception exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessage.builder().code(INVALID_CUSTOMER).message(exception.getMessage()).build());
    }

    @ExceptionHandler(value = {
            Exception.class
    })
    protected ResponseEntity<ErrorMessage> handleSystemError(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorMessage.builder().code(INTERNAL_SERVER_ERROR).message(exception.getMessage()).build());
    }
}
