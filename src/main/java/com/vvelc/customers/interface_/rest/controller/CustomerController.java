package com.vvelc.customers.interface_.rest.controller;

import com.vvelc.customers.application.model.PageRequest;
import com.vvelc.customers.application.model.PageResponse;
import com.vvelc.customers.application.service.CustomerService;
import com.vvelc.customers.domain.model.Customer;
import com.vvelc.customers.interface_.rest.dto.CustomerCreateRequest;
import com.vvelc.customers.interface_.rest.dto.CustomerResponse;
import com.vvelc.customers.interface_.rest.dto.CustomerUpdateRequest;
import com.vvelc.customers.interface_.rest.mapper.CustomerDtoMapper;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Customers", description = "Operaciones relacionadas con clientes")
@Path(CustomerController.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    protected static final String PATH = "/customers";

    @Inject
    CustomerService customerService;

    @POST
    @Operation(summary = "Crear un nuevo cliente")
    @APIResponse(responseCode = "201", description = "Cliente creado exitosamente", content = @Content(
            schema = @Schema(implementation = CustomerResponse.class)
    ))
    @RequestBody(
            content = @Content(
                    schema = @Schema(implementation = CustomerCreateRequest.class)
            )
    )
    @APIResponse(responseCode = "404", description = "No se encontró el cliente luego de crearlo")
    public Response createCustomer(@Valid CustomerCreateRequest request) {
        Log.infof("Received customer creation request for: %s %s %s %s ",
                request.firstName(), request.secondName(), request.firstLastName(), request.secondLastName());

        Long id = customerService.createCustomer(
                request.firstName(),
                request.secondName(),
                request.firstLastName(),
                request.secondLastName(),
                request.email(),
                request.address(),
                request.phone(),
                request.country()
        );

        Customer customer = customerService.getCustomerById(id);
        URI location = URI.create(String.format("%s/%s", CustomerController.PATH, customer.getId()));

        CustomerResponse customerResponse = CustomerDtoMapper.toDto(customer);

        return Response
                .created(location)
                .entity(customerResponse)
                .build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener un cliente por ID")
    @APIResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(
            schema = @Schema(implementation = CustomerResponse.class)
    ))
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response getById(@PathParam("id") UUID id) {
        Log.info("Received request to get customer by ID: " + id);

        Customer customer = customerService.getCustomerById(id);

        CustomerResponse customerResponse = CustomerDtoMapper.toDto(customer);

        return Response.ok(customerResponse).build();
    }

    @GET
    @Operation(summary = "Obtener todos los clientes")
    @APIResponse(responseCode = "200", description = "Lista de clientes", content = @Content(
            schema = @Schema(
                    implementation = PageResponse.class,
                    oneOf = CustomerResponse.class
            )
    ))
    public Response getAll(
            @QueryParam("country") String country,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ) {
        final PageRequest pageRequest = new PageRequest(page, size);
        final PageResponse<Customer> customers;

        // If a country is provided, filter customers by country
        // Otherwise, return all customers
        if (country != null && !country.isBlank()) {
            Log.info("Received request to get customers by country: " + country);
            customers = customerService.getCustomersByCountry(country, pageRequest);
        } else {
            Log.info("Received request to get all customers");
            customers = customerService.getAllCustomers(pageRequest);
        }

        final PageResponse<CustomerResponse> pageResponse = new PageResponse<>(
                customers.items().stream()
                        .map(CustomerDtoMapper::toDto)
                        .toList(),
                customers.page(),
                customers.size(),
                customers.total()

        return Response.ok(pageResponse)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar un cliente")
    @APIResponse(responseCode = "200", description = "Cliente actualizado exitosamente", content = @Content(
            schema = @Schema(implementation = CustomerResponse.class)
    ))
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response updateCustomer(@PathParam("id") Long id, @Valid CustomerUpdateRequest request) {
        Log.info("Received customer update request for ID: " + id);

        Customer customer = customerService.updateCustomer(
                id,
                request.email(),
                request.address(),
                request.phone(),
                request.country()
        );

        CustomerResponse customerResponse = CustomerDtoMapper.toDto(customer);

        return Response.ok(customerResponse)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar un cliente")
    @APIResponse(responseCode = "204", description = "Cliente eliminado exitosamente")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response deleteCustomer(@PathParam("id") Long id) {
        Log.info("Received request to delete customer with ID: " + id);

        customerService.deleteCustomer(id);

        return Response.noContent().build();
    }
}
