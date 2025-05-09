package com.vvelc.customers.interface_.rest.mapper;

import com.vvelc.booking.domain.model.Booking;
import com.vvelc.booking.interface_.rest.dto.BookingResponse;

public class BookingDtoMapper {
    public static BookingResponse toDto(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getRoomId(),
                booking.getCustomerName(),
                booking.getCheckIn(),
                booking.getCheckOut()
        );
    }
}