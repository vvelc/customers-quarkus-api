package com.vvelc.customers.interface_.rest.dto;

import com.vvelc.booking.interface_.rest.validation.ValidDateRange;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@ValidDateRange
public record BookingOrderRequest(
        @NotNull(message = "Room ID is required")
        UUID roomId,

        @NotNull(message = "Customer name is required")
        @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
        String customerName,

        @NotNull(message = "Check-in date is required")
        @Future(message = "Check-in date must be in the future")
        LocalDate checkIn,

        @NotNull(message = "Check-out date is required")
        @Future(message = "Check-out date must be in the future")
        LocalDate checkOut
) {}
