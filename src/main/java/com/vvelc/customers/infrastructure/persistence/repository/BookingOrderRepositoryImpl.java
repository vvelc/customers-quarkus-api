package com.vvelc.customers.infrastructure.persistence.repository;

import com.vvelc.booking.domain.common.BookingStatus;
import com.vvelc.booking.domain.model.BookingOrder;
import com.vvelc.booking.domain.repository.BookingOrderRepository;
import com.vvelc.booking.infrastructure.persistence.entity.BookingOrderEntity;
import com.vvelc.booking.infrastructure.persistence.mapper.BookingOrderMapper;
import com.vvelc.booking.infrastructure.persistence.panache.BookingOrderPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BookingOrderRepositoryImpl implements BookingOrderRepository {

    @Inject
    BookingOrderPanacheRepository bookingOrderPanacheRepository;

    @Override
    public void save(BookingOrder order) {
        BookingOrderEntity entity = BookingOrderMapper.toEntity(order);
        bookingOrderPanacheRepository.persist(entity);
    }

    @Override
    public List<BookingOrder> findAll() {
        return bookingOrderPanacheRepository.findAll().list().stream()
                .map(BookingOrderMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<BookingOrder> findById(UUID id) {
        return bookingOrderPanacheRepository.findByIdOptional(id)
                .map(BookingOrderMapper::toDomain);
    }

    @Override
    public List<BookingOrder> findByRoomId(UUID roomId) {
        return bookingOrderPanacheRepository.find("roomId", roomId).list().stream()
                .map(BookingOrderMapper::toDomain)
                .toList();
    }

    @Override
    public void updateStatus(UUID id, String status) {
        bookingOrderPanacheRepository.findByIdOptional(id).ifPresent(entity ->
                entity.setStatus(BookingStatus.valueOf(status))
        );
    }

    @Override
    public boolean deleteById(UUID id) {
        return bookingOrderPanacheRepository.deleteById(id);
    }
}
