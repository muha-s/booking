package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("""
            select hotel
            from Hotel hotel
            join hotel.city city
            where hotel.deletedAt is null
              and city.deletedAt is null
            """)
    List<Hotel> findAllActive();

    @Query("""
            select hotel
            from Hotel hotel
            join hotel.city city
            where hotel.id = :id
              and hotel.deletedAt is null
              and city.deletedAt is null
            """)
    Optional<Hotel> findActiveById(Long id);

}
