package com.vvelc.customers.interface_.rest.controller;

import com.vvelc.booking.application.service.BookingService;
import com.vvelc.booking.interface_.rest.dto.BookingResponse;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

@Tag(name = "Bookings", description = "Operaciones relacionadas con reservas de habitaciones")
@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingController {

    @Inject
    BookingService bookingService;

    @GET
    @Operation(summary = "Obtener todos los bookings")
    @APIResponse(responseCode = "200", description = "Bookings encontrados", content = @Content(
            schema = @Schema(type = SchemaType.ARRAY, implementation = BookingResponse.class)
    ))
    public Response getAll() {
        Log.info("Received request to get all bookings");
        return Response.ok(bookingService.getAllBookings())
                .build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener un booking por ID")
    @APIResponse(responseCode = "200", description = "Booking encontrado", content = @Content(
            schema = @Schema(implementation = BookingResponse.class)
    ))
    @APIResponse(responseCode = "404", description = "Booking no encontrado")
    public Response getById(@PathParam("id") UUID id) {
        Log.info("Received request to get booking by ID: " + id);
        return Response.ok(bookingService.getBookingById(id))
                .build();
    }
}
