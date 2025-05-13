package com.vvelc.customers.interface_.rest.mapper;

import com.vvelc.customers.domain.model.Customer;
import com.vvelc.customers.interface_.rest.dto.CustomerCreateRequest;
import com.vvelc.customers.interface_.rest.dto.CustomerResponse;
import com.vvelc.customers.interface_.rest.dto.CustomerUpdateRequest;

public class CustomerDtoMapper {
    private CustomerDtoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Customer toDomain(CustomerCreateRequest request) {
        return new Customer(
                null,
                request.firstName(),
                request.secondName(),
                request.firstLastName(),
                request.secondLastName(),
                request.email(),
                request.address(),
                request.phone(),
                request.country(),
                null
        );
    }

    public static Customer toDomain(CustomerUpdateRequest request) {
        return new Customer(
                null,
                null,
                null,
                null,
                null,
                request.email(),
                request.address(),
                request.phone(),
                request.country(),
                null
        );
    }

    public static CustomerResponse toDto(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getSecondName(),
                customer.getFirstLastName(),
                customer.getSecondLastName(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhone(),
                customer.getCountry(),
                customer.getDemonym()
        );
    }
}
