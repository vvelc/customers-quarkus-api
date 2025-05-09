package com.vvelc.customers.infrastructure.persistence.entity;


import com.vvelc.booking.domain.common.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "booking_orders")
@Getter
@Setter
public class BookingOrderEntity {

    @Id
    private UUID id;

    private UUID roomId;
    private String customerName;
    private LocalDate checkIn;
    private LocalDate checkOut;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}