package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.Room;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {


    @Query("""
            select room
            from Room room
            join room.hotel hotel
            join hotel.city city
            where room.id = :id
              and room.deletedAt is null
              and hotel.deletedAt is null
              and city.deletedAt is null
            """)
    Optional<Room> findActiveById(Long id);

    @Query("""
            select room
            from Room room
            join room.hotel hotel
            join hotel.city city
            where hotel.id = :hotelId
              and room.roomType = :roomType
              and room.roomCapacity = :roomCapacity
              and room.deletedAt is null
              and hotel.deletedAt is null
              and city.deletedAt is null
              and not exists (
                  select booking.id
                  from Booking booking
                  where booking.room = room
                    and booking.status = :status
                    and booking.startDate < :endDate
                    and booking.endDate > :startDate
              )
            """)
    List<Room> findSuitableActiveRooms(
            Long hotelId,
            RoomType roomType,
            RoomCapacity roomCapacity,
            LocalDate startDate,
            LocalDate endDate,
            BookingStatus status
    );

    @Query("""
            select room
            from Room room
            join room.hotel hotel
            join hotel.city city
            where hotel.id = :hotelId
              and room.roomType = :roomType
              and room.roomCapacity = :roomCapacity
              and room.deletedAt is null
              and hotel.deletedAt is null
              and city.deletedAt is null
              and not exists (
                  select booking.id
                  from Booking booking
                  where booking.room = room
                    and booking.status = :status
                    and booking.id <> :bookingId
                    and booking.startDate < :endDate
                    and booking.endDate > :startDate
              )
            """)
    List<Room> findSuitableActiveRoomsForUpdate(
            Long hotelId,
            RoomType roomType,
            RoomCapacity roomCapacity,
            LocalDate startDate,
            LocalDate endDate,
            BookingStatus status,
            Long bookingId
    );

    @Query("""
            select case when count(room) > 0 then true else false end
            from Room room
            join room.hotel hotel
            join hotel.city city
            where hotel.id = :hotelId
              and room.roomType = :roomType
              and room.roomCapacity = :roomCapacity
              and room.deletedAt is null
              and hotel.deletedAt is null
              and city.deletedAt is null
            """)
    boolean existsRoomByHotelIdAndRoomTypeAndRoomCapacity(
            Long hotelId,
            RoomType roomType,
            RoomCapacity roomCapacity
    );

    @Query("""
            select room
            from Room room
            join room.hotel hotel
            join hotel.city city
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
    List<Room> findAvailableRooms(
            Long cityId,
            LocalDate startDate,
            LocalDate endDate,
            RoomCapacity roomCapacity,
            RoomType roomType
    );
}
