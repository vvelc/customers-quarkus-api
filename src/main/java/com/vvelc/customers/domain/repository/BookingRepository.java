package com.vvelc.customers.domain.repository;

import com.vvelc.booking.domain.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository  {
    void save(Booking booking);
    List<Booking> findAll();
    Optional<Booking> findById(UUID id);
    List<Booking> findByRoomId(UUID roomId);
    boolean hasOverlappingBooking(UUID roomId, LocalDate checkIn, LocalDate checkOut);
    boolean deleteById(UUID id);
}