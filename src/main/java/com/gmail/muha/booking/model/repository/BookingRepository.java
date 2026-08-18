package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            select booking
            from Booking booking
            where booking.status = :status
              and booking.endDate <= :currentDate
            """)
    List<Booking> findExpiredBookings(
            @Param("status") BookingStatus status,
            @Param("currentDate") LocalDate currentDate);

    @Query("""
            select booking
            from Booking booking
            where booking.status = :status
              and booking.reviewRequestSentAt is null
            """)
    List<Booking> findBookingsForReviewRequest(@Param("status") BookingStatus status);

    @Query("""
            select booking
            from Booking booking
            where booking.user.id = :userId
              and booking.status = com.gmail.muha.booking.model.entity.enums.BookingStatus.ACTIVE
              and booking.startDate >= CURRENT_DATE
            """)
    List<Booking> findFutureActiveBookingsByUserId(@Param("userId") Long userId);

    @Query("""
            select booking
            from Booking booking
            where booking.room.hotel.city.id = :cityId
              and booking.status = com.gmail.muha.booking.model.entity.enums.BookingStatus.ACTIVE
              and booking.startDate >= CURRENT_DATE
            """)
    List<Booking> findFutureActiveBookingsByCityId(@Param("cityId") Long cityId);

    @Query("""
            select booking
            from Booking booking
            where booking.room.hotel.id = :hotelId
              and booking.status = com.gmail.muha.booking.model.entity.enums.BookingStatus.ACTIVE
              and booking.startDate >= CURRENT_DATE
            """)
    List<Booking> findFutureActiveBookingsByHotelId(@Param("hotelId") Long hotelId);

    @Query("""
            select booking
            from Booking booking
            where booking.room.id = :roomId
              and booking.status = com.gmail.muha.booking.model.entity.enums.BookingStatus.ACTIVE
              and booking.startDate >= CURRENT_DATE
            """)
    List<Booking> findFutureActiveBookingsByRoomId(@Param("roomId") Long roomId);
}