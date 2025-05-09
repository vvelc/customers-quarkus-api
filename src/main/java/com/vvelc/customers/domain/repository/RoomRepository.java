package com.vvelc.customers.domain.repository;

import com.vvelc.booking.domain.model.Room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {
    void save(Room room);
    List<Room> findAll();
    Optional<Room> findById(UUID id);
    // TODO: implement find available rooms for a given date range
    void deleteById(UUID id);
}