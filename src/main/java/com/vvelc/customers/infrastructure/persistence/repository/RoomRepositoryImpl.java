package com.vvelc.customers.infrastructure.persistence.repository;

import com.vvelc.booking.domain.model.Room;
import com.vvelc.booking.domain.repository.RoomRepository;
import com.vvelc.booking.infrastructure.persistence.entity.RoomEntity;
import com.vvelc.booking.infrastructure.persistence.mapper.RoomMapper;
import com.vvelc.booking.infrastructure.persistence.panache.RoomPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RoomRepositoryImpl implements RoomRepository {
    @Inject
    RoomPanacheRepository roomPanacheRepository;

    @Override
    public void save(Room room) {
        RoomEntity roomEntity = RoomMapper.toEntity(room);
        roomPanacheRepository.persist(roomEntity);
    }

    @Override
    public List<Room> findAll() {
        return roomPanacheRepository.listAll().stream()
                .map(RoomMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Room> findById(UUID id) {
        return roomPanacheRepository.findByIdOptional(id)
                .map(RoomMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        roomPanacheRepository.deleteById(id);
    }
}