package com.vvelc.customers.domain.exception;

import jakarta.ws.rs.core.Response;

public class BadRequestException extends PublicException {
    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return Response.Status.BAD_REQUEST.getStatusCode();
    }
}
