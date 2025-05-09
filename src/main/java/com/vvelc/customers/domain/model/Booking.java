package com.vvelc.customers.domain.model;

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
public class Booking {
    private UUID id;
    private UUID roomId;
    private String customerName;
    private LocalDate checkIn;
    private LocalDate checkOut;
}