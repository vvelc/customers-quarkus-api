package com.vvelc.customers.infrastructure.exception;

import com.vvelc.customers.domain.exception.PublicException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
@ApplicationScoped
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof PublicException publicException) {
            return Response.status(publicException.getStatusCode())
                    .entity(new ErrorResponse(publicException.getClass().getSimpleName(), publicException.getMessage()))
                    .build();
        }

        if (exception.getCause() instanceof jakarta.ws.rs.ClientErrorException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("ClientError", exception.getMessage()))
                    .build();
        }

        LOG.error("Unhandled exception", exception);

        boolean isDev = "dev".equals(System.getProperty("quarkus.profile", "prod"));

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(
                        "InternalServerError",
                        isDev ? exception.getMessage() : "Unexpected error occurred"))
                .build();
    }

    public record ErrorResponse(String error, String message) {}
}
