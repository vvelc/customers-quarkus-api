package com.vvelc.customers.interface_.rest.dto;

import java.time.LocalDate;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        UUID roomId,
        String customerName,
        LocalDate checkIn,
        LocalDate checkOut
) {}