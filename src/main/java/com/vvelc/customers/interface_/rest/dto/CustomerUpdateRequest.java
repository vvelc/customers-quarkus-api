package com.vvelc.customers.interface_.rest.dto;

import com.vvelc.customers.interface_.rest.dto.shared.HasCountry;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(
        @Email(message = "Email must be a valid email address")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address,

        @Size(max = 20, message = "Phone must not exceed 20 characters")
        String phone,

        @Size(min = 2, max = 2, message = "Country must be exactly 2 characters")
        @Pattern(regexp = "^[A-Z]{2}$", message = "Country must be in ISO 3166-1 alpha-2 format (uppercase letters)")
        String country
) implements HasCountry {}
