package com.vvelc.customers.infrastructure.persistence.panache;

import com.vvelc.customers.infrastructure.persistence.entity.CustomerEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class CustomerPanacheRepository implements PanacheRepositoryBase<CustomerEntity, UUID> {
}