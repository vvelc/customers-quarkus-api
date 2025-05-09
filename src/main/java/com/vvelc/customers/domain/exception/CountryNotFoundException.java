package com.vvelc.customers.domain.exception;

public class CountryNotFoundException extends NotFoundException {
    public CountryNotFoundException(String message) {
        super(message);
    }
}
