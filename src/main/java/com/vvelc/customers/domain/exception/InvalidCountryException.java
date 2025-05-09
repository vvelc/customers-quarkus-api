package com.vvelc.customers.domain.exception;

public class InvalidCountryException extends BadRequestException {
    public InvalidCountryException(String message) {
        super(message);
    }
}
