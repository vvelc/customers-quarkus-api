package com.vvelc.customers.infrastructure.adapter.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://restcountries.com")
public interface CountryValidationApiClient {
    @GET
    @Path("/v3.1/alpha/{code}")
    Response getCountryByCode(@PathParam("code") String code);
}
