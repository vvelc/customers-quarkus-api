package com.vvelc.customers.infrastructure.persistence.panache;

import com.vvelc.booking.infrastructure.persistence.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class BookingPanacheRepository implements PanacheRepositoryBase<BookingEntity, UUID> {
}