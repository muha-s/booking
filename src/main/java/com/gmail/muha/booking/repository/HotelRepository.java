package com.gmail.muha.booking.repository;

import com.gmail.muha.booking.entity.Hotel;

import java.util.List;
import java.util.Optional;

public interface HotelRepository {

    Hotel save(Hotel hotel);

    Optional<Hotel> findById(Long id);

    List<Hotel> findAll();

    void deleteById(Long id);

    Hotel update(Hotel hotel);


}
