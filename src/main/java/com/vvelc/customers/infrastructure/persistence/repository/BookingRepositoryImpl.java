package com.vvelc.customers.infrastructure.persistence.repository;

import com.vvelc.booking.domain.model.Booking;
import com.vvelc.booking.domain.repository.BookingRepository;
import com.vvelc.booking.infrastructure.persistence.entity.BookingEntity;
import com.vvelc.booking.infrastructure.persistence.mapper.BookingMapper;
import com.vvelc.booking.infrastructure.persistence.panache.BookingPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BookingRepositoryImpl implements BookingRepository {

    @Inject
    BookingPanacheRepository bookingPanacheRepository;

    @Override
    public void save(Booking booking) {
        BookingEntity bookingEntity = BookingMapper.toEntity(booking);
        bookingPanacheRepository.persist(bookingEntity);
    }

    @Override
    public boolean hasOverlappingBooking(UUID roomId, LocalDate checkIn, LocalDate checkOut) {
        return bookingPanacheRepository.count("roomId = ?1 AND ((checkIn <= ?3 AND checkOut >= ?2))",
                roomId, checkIn, checkOut) > 0;
    }

    @Override
    public List<Booking> findAll() {
        return bookingPanacheRepository.listAll().stream()
                .map(BookingMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return bookingPanacheRepository.findByIdOptional(id)
                .map(BookingMapper::toDomain);
    }

    @Override
    public List<Booking> findByRoomId(UUID roomId) {
        return bookingPanacheRepository.find("roomId", roomId).stream()
                .map(BookingMapper::toDomain)
                .toList();
    }

    @Override
    public boolean deleteById(UUID id) {
        return bookingPanacheRepository.deleteById(id);
    }
}