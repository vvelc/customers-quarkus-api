package com.vvelc.customers.domain.exception;

public abstract class PublicException extends RuntimeException {
    protected PublicException(String message) {
        super(message);
    }

    public abstract int getStatusCode();
}
