package com.vvelc.customers.interface_.rest.controller.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomerQueryParams {
    @QueryParam("country")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country must be in ISO 3166-1 alpha-2 format (uppercase letters)")
    private String country;

    @QueryParam("page")
    @Min(0)
    @DefaultValue("0")
    private int page;

    @QueryParam("size")
    @Min(1)
    @Max(100)
    @DefaultValue("10")
    private int size;
}
