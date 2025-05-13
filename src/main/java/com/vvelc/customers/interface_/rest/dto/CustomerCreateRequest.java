package com.vvelc.customers.interface_.rest.dto;

import com.vvelc.customers.interface_.rest.dto.shared.HasCountry;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerCreateRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must not exceed 50 characters")
        String firstName,

        @Size(max = 50, message = "Second name must not exceed 50 characters")
        String secondName,

        @NotBlank(message = "First last name is required")
        @Size(max = 50, message = "First last name must not exceed 50 characters")
        String firstLastName,

        @Size(max = 50, message = "Second last name must not exceed 50 characters")
        String secondLastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address,

        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "^\\+?[0-9\\-\\s()]*$", message = "Phone must be a valid phone number")
        @Size(min = 7, max = 20, message = "Phone must be between 7 and 20 characters")
        String phone,

        @NotBlank(message = "Country is required")
        @Size(min = 2, max = 2, message = "Country must be exactly 2 characters")
        @Pattern(regexp = "^[A-Z]{2}$", message = "Country must be in ISO 3166-1 alpha-2 format (uppercase letters)")
        String country
) implements HasCountry {
}
