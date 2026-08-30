package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
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

    @Query("""
       select distinct hotel
       from Hotel hotel
       join hotel.city city
       join hotel.rooms room
       where city.id = :cityId
         and city.deletedAt is null
         and hotel.deletedAt is null
         and room.deletedAt is null
         and (:roomCapacity is null or room.roomCapacity = :roomCapacity)
         and (:roomType is null or room.roomType = :roomType)
         and not exists (
             select booking.id
             from Booking booking
             where booking.room = room
               and booking.status =
                   com.gmail.muha.booking.model.entity.enums.BookingStatus.ACTIVE
               and booking.startDate < :endDate
               and booking.endDate > :startDate
         )
       """)
    List<Hotel> findAvailableHotels(
            Long cityId,
            LocalDate startDate,
            LocalDate endDate,
            RoomCapacity roomCapacity,
            RoomType roomType
    );
}
