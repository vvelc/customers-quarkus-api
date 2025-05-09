package com.vvelc.customers.domain.exception;

import jakarta.ws.rs.core.Response;

public class CountryServiceException extends RuntimeException {
    public CountryServiceException(String message) {
        super(message);
    }

    public int getStatusCode() {
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }
}
