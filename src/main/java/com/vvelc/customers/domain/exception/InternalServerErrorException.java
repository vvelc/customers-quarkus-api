package com.vvelc.customers.domain.exception;

import jakarta.ws.rs.core.Response;

public class InternalServerErrorException extends PublicException {
    public InternalServerErrorException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }
}
