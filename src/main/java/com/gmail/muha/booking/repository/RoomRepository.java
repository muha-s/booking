package com.gmail.muha.booking.repository;

import com.gmail.muha.booking.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Room save(Room room);

    Optional<Room> findById(Long id);

    List<Room> findAll();

    void deleteById(Long id);

    Room update(Room room);
}
