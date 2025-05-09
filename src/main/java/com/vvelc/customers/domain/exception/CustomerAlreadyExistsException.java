package com.vvelc.customers.domain.exception;

public class CustomerAlreadyExistsException extends ConflictException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
