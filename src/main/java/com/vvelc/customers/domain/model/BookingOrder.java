package com.vvelc.customers.domain.model;

import com.vvelc.booking.domain.common.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingOrder {
    private UUID id;
    private UUID roomId;
    private String customerName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BookingStatus status;
}