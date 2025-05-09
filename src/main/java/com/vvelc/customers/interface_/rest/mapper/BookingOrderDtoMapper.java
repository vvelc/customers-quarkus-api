package com.vvelc.customers.interface_.rest.mapper;

import com.vvelc.booking.domain.model.BookingOrder;
import com.vvelc.booking.interface_.rest.dto.BookingOrderResponse;

public class BookingOrderDtoMapper {
    public static BookingOrderResponse toDto(BookingOrder order) {
        return new BookingOrderResponse(
                order.getId(),
                order.getRoomId(),
                order.getCustomerName(),
                order.getCheckIn(),
                order.getCheckOut(),
                order.getStatus().name()
        );
    }
}
