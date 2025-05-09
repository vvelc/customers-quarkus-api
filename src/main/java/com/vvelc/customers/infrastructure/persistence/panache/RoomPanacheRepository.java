package com.vvelc.customers.infrastructure.persistence.panache;

import com.vvelc.booking.infrastructure.persistence.entity.RoomEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class RoomPanacheRepository implements PanacheRepositoryBase<RoomEntity, UUID> {
}