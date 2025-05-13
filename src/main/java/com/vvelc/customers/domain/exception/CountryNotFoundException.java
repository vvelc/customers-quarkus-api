package com.vvelc.customers.domain.exception;

public class CountryNotFoundException extends BadRequestException {
    public CountryNotFoundException(String message) {
        super(message);
    }
}
