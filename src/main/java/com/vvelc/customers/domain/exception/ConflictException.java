package com.vvelc.customers.domain.exception;

import jakarta.ws.rs.core.Response;

public class ConflictException extends PublicException {
    public ConflictException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return Response.Status.CONFLICT.getStatusCode();
    }
}
