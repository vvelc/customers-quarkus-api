package com.vvelc.customers.infrastructure.adapter.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

// infrastructure/adapter/rest/CountryApiClient.java
@RegisterRestClient(baseUri = "https://restcountries.com")
public interface CountryValidationApiClient {
    @GET
    @Path("/v3.1/alpha/{code}")
    Response getCountryByCode(@PathParam("code") String code);
}
