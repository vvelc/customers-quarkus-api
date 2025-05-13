package com.vvelc.customers.infrastructure.persistence.mapper;

import com.vvelc.customers.domain.model.Customer;
import com.vvelc.customers.infrastructure.persistence.entity.CustomerEntity;

public class CustomerMapper {

    public static Customer toDomain(CustomerEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Customer(
                entity.getId(),
                entity.getFirstName(),
                entity.getSecondName(),
                entity.getFirstLastName(),
                entity.getSecondLastName(),
                entity.getEmail(),
                entity.getAddress(),
                entity.getPhone(),
                entity.getCountry(),
                entity.getDemonym()
        );
    }

    public static CustomerEntity toEntity(Customer domain) {
        if (domain == null) {
            return null;
        }

        CustomerEntity entity = new CustomerEntity();

        entity.setId(domain.getId());
        entity.setFirstName(domain.getFirstName());
        entity.setSecondName(domain.getSecondName());
        entity.setFirstLastName(domain.getFirstLastName());
        entity.setSecondLastName(domain.getSecondLastName());
        entity.setEmail(domain.getEmail());
        entity.setAddress(domain.getAddress());
        entity.setPhone(domain.getPhone());
        entity.setCountry(domain.getCountry());
        entity.setDemonym(domain.getDemonym());

        return entity;
    }
}