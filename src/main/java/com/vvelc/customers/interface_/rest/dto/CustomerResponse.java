package com.vvelc.customers.interface_.rest.dto;

public record CustomerResponse(
        Long id,
        String firstName,
        String secondName,
        String firstLastName,
        String secondLastName,
        String email,
        String address,
        String phone,
        String country,
        String demonym
) {
}
