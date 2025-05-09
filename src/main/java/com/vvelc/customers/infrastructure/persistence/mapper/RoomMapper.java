// Mapper para Room ↔ RoomEntity
package com.vvelc.customers.infrastructure.persistence.mapper;

import com.vvelc.booking.domain.model.Room;
import com.vvelc.booking.infrastructure.persistence.entity.RoomEntity;

public class RoomMapper {

    public static Room toDomain(RoomEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Room(
                entity.getId(),
                entity.getNumber()
        );
    }

    public static RoomEntity toEntity(Room domain) {
        if (domain == null) {
            return null;
        }
        RoomEntity entity = new RoomEntity();
        entity.setId(domain.getId());
        return entity;
    }
}