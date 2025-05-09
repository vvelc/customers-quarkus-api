package com.vvelc.customers.interface_.rest.controller;

import com.vvelc.booking.application.service.RoomService;
import com.vvelc.booking.interface_.rest.dto.RoomRequest;
import com.vvelc.booking.interface_.rest.dto.RoomResponse;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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

@Tag(name = "Rooms", description = "Operaciones relacionadas con habitaciones")
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomController {

    @Inject
    RoomService roomService;

    @POST
    @Operation(summary = "Crear una nueva habitación")
    @APIResponse(responseCode = "200", description = "Habitación creada exitosamente", content = @Content(
            schema = @Schema(implementation = RoomResponse.class)
    ))
    @APIResponse(responseCode = "404", description = "Habitación no encontrada luego de crearla")
    public Response create(@Valid RoomRequest request) {
        Log.info("Received room creation request for room number: " + request.number());
        UUID id = roomService.createRoom(request.number());

        return Response.ok(roomService.getRoomById(id))
                .build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener una habitación por ID")
    @APIResponse(responseCode = "200", description = "Habitación encontrada", content = @Content(
            schema = @Schema(implementation = RoomResponse.class)
    ))
    @APIResponse(responseCode = "404", description = "Habitación no encontrada")
    public Response getById(@PathParam("id") UUID id) {
        Log.info("Received request to get room by ID: " + id);
        return Response.ok(roomService.getRoomById(id))
                .build();
    }

    @GET
    @Operation(summary = "Obtener todas las habitaciones")
    @APIResponse(responseCode = "200", description = "Lista de habitaciones", content = @Content(
            schema = @Schema(type = SchemaType.ARRAY, implementation = RoomResponse.class)
    ))
    public Response getAll() {
        Log.info("Received request to get all rooms");
        return Response.ok(roomService.getAllRooms())
                .build();
    }
}
