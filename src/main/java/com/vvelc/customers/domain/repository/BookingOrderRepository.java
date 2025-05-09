package com.vvelc.customers.domain.repository;

import com.vvelc.booking.domain.model.BookingOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingOrderRepository {
    void save(BookingOrder order);
    List<BookingOrder> findAll();
    Optional<BookingOrder> findById(UUID id);
    List<BookingOrder> findByRoomId(UUID roomId);
    void updateStatus(UUID id, String status);
    boolean deleteById(UUID id);
}