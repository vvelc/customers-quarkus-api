package com.vvelc.customers.interface_.rest.mapper;

import com.vvelc.customers.domain.model.Customer;
import com.vvelc.customers.interface_.rest.dto.CustomerResponse;

public class CustomerDtoMapper {
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