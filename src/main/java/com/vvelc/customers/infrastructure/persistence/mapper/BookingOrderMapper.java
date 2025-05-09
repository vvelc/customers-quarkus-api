// Mapper para BookingOrder ↔ BookingOrderEntity
package com.vvelc.customers.infrastructure.persistence.mapper;

import com.vvelc.booking.domain.common.BookingStatus;
import com.vvelc.booking.domain.model.BookingOrder;
import com.vvelc.booking.infrastructure.persistence.entity.BookingOrderEntity;

public class BookingOrderMapper {

    public static BookingOrder toDomain(BookingOrderEntity entity) {
        if (entity == null) {
            return null;
        }
        return new BookingOrder(
                entity.getId(),
                entity.getRoomId(),
                entity.getCustomerName(),
                entity.getCheckIn(),
                entity.getCheckOut(),
                BookingStatus.valueOf(entity.getStatus().name())
        );
    }

    public static BookingOrderEntity toEntity(BookingOrder domain) {
        if (domain == null) {
            return null;
        }
        BookingOrderEntity entity = new BookingOrderEntity();
        entity.setId(domain.getId());
        entity.setRoomId(domain.getRoomId());
        entity.setCustomerName(domain.getCustomerName());
        entity.setCheckIn(domain.getCheckIn());
        entity.setCheckOut(domain.getCheckOut());
        entity.setStatus(BookingStatus.valueOf(domain.getStatus().name()));
        return entity;
    }
}