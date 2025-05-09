package com.vvelc.customers.infrastructure.persistence.mapper;

import com.vvelc.booking.domain.model.Booking;
import com.vvelc.booking.infrastructure.persistence.entity.BookingEntity;

public class BookingMapper {

    public static Booking toDomain(BookingEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Booking(
                entity.getId(),
                entity.getRoomId(),
                entity.getCustomerName(),
                entity.getCheckIn(),
                entity.getCheckOut()
        );
    }

    public static BookingEntity toEntity(Booking domain) {
        if (domain == null) {
            return null;
        }
        BookingEntity entity = new BookingEntity();
        entity.setId(domain.getId());
        entity.setRoomId(domain.getRoomId());
        entity.setCustomerName(domain.getCustomerName());
        entity.setCheckIn(domain.getCheckIn());
        entity.setCheckOut(domain.getCheckOut());
        return entity;
    }
}
