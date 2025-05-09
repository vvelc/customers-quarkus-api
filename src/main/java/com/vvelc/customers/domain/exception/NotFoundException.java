package com.vvelc.customers.domain.exception;

import jakarta.ws.rs.core.Response;

public class NotFoundException extends PublicException {
    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return Response.Status.NOT_FOUND.getStatusCode();
    }
}
