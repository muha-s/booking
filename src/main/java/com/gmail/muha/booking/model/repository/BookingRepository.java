package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


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

    @Query("""
            select booking
            from Booking booking
            where booking.user.email = :userEmail
            order by
                case
                    when booking.status = com.gmail.muha.booking.model.entity.enums.BookingStatus.ACTIVE then 0
                    when booking.status = com.gmail.muha.booking.model.entity.enums.BookingStatus.COMPLETED then 1
                    when booking.status = com.gmail.muha.booking.model.entity.enums.BookingStatus.CANCELLED then 2
                    else 3
                end,
                booking.startDate desc
            """)
    Page<Booking> findAllByUserEmail(@Param("userEmail") String userEmail, Pageable pageable);

    @Query("""
            select booking
            from Booking booking
            where booking.id = :id
              and booking.user.email = :userEmail
            """)
    Optional<Booking> findByIdAndUserEmail(@Param("id") Long id, @Param("userEmail") String userEmail);

    @Query(
            value = """
                    select booking
                    from Booking booking
                    join fetch booking.user
                    join fetch booking.room room
                    where room.hotel.id = :hotelId
                    order by booking.startDate desc
                    """,
            countQuery = """
                    select count(booking)
                    from Booking booking
                    join booking.room room
                    where room.hotel.id = :hotelId
                    """
    )
    Page<Booking> findAllByHotelId(@Param("hotelId") Long hotelId, Pageable pageable);
}