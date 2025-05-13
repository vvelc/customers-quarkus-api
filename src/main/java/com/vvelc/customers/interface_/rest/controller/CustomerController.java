package com.vvelc.customers.interface_.rest.controller;

import com.vvelc.customers.application.model.PageRequest;
import com.vvelc.customers.application.model.PageResponse;
import com.vvelc.customers.application.service.CustomerService;
import com.vvelc.customers.domain.model.Customer;
import com.vvelc.customers.interface_.rest.controller.query.CustomerQueryParams;
import com.vvelc.customers.interface_.rest.dto.CustomerCreateRequest;
import com.vvelc.customers.interface_.rest.dto.CustomerPageResponse;
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
import java.util.Optional;

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
    @APIResponse(responseCode = "400", description = "Error de validación de datos")
    @APIResponse(responseCode = "404", description = "No se encontró el cliente luego de crearlo")
    @RequestBody(
            content = @Content(
                    schema = @Schema(implementation = CustomerCreateRequest.class)
            )
    )
    public Response createCustomer(@Valid CustomerCreateRequest request) {
        Log.infof("Received customer creation request for: %s %s %s %s ",
                request.firstName(), request.secondName(), request.firstLastName(), request.secondLastName());

        final Customer customer = CustomerDtoMapper.toDomain(request);

        Customer createdCustomer = customerService.createCustomer(customer);

        URI location = URI.create(String.format("%s/%s", CustomerController.PATH, createdCustomer.getId()));
        CustomerResponse customerResponse = CustomerDtoMapper.toDto(createdCustomer);

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
    public Response getById(@PathParam("id") Long id) {
        Log.info("Received request to get customer by ID: " + id);

        Customer customer = customerService.getCustomerById(id);

        CustomerResponse customerResponse = CustomerDtoMapper.toDto(customer);

        return Response.ok(customerResponse).build();
    }

    @GET
    @Operation(summary = "Obtener todos los clientes")
    @APIResponse(responseCode = "200", description = "Lista de clientes", content = @Content(
            schema = @Schema(
                    implementation = CustomerPageResponse.class
            )
    ))
    public Response getAll(@Valid @BeanParam CustomerQueryParams queryParams) {
        final PageRequest pageRequest = new PageRequest(queryParams.getPage(), queryParams.getSize());
        final PageResponse<Customer> customers;

        // If a country is provided, filter customers by country
        // Otherwise, return all customers
        String country = Optional.ofNullable(queryParams.getCountry())
                .map(String::toUpperCase)
                .orElse(null);
        if (country != null && !country.isBlank()) {
            Log.info("Received request to get customers by country: " + country);
            customers = customerService.getCustomersByCountry(country, pageRequest);
        } else {
            Log.info("Received request to get all customers");
            customers = customerService.getAllCustomers(pageRequest);
        }

        final CustomerPageResponse pageResponse = new CustomerPageResponse(
                customers.getItems().stream()
                        .map(CustomerDtoMapper::toDto)
                        .toList(),
                customers.getPage(),
                customers.getSize(),
                customers.getTotal()
        );

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
        Customer customer = CustomerDtoMapper.toDomain(request);

        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        CustomerResponse customerResponse = CustomerDtoMapper.toDto(updatedCustomer);

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
