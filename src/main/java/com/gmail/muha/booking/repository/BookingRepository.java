package com.gmail.muha.booking.repository;

import com.gmail.muha.booking.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    Booking save(Booking booking);

    Optional<Booking> findById(Long id);

    List<Booking> findAll();

    void deleteById(Long id);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByRoomId(Long roomId);

    Booking update(Booking booking);
}
