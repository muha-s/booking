package com.gmail.muha.booking.service.booking;

import com.gmail.muha.booking.dto.booking.*;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;

import java.util.List;

public interface BookingService {

    BookingFullDto findById(Long id);

    Booking findEntityById(Long id);

    List<BookingShortDto> findAll();

    List<BookingForUserDto> findAllByUserEmail(String userEmail);

    BookingFullDto create(BookingCreateDto bookingCreateDto, String userEmail);

    BookingFullDto update(Long id, BookingUpdateDto bookingUpdateDto);

    void completeExpiredBookings();

    List<Booking> findFutureActiveBookingsByUserId(Long userId);

    List<Booking> findFutureActiveBookingsByCityId(Long cityId);

    List<Booking> findFutureActiveBookingsByHotelId(Long cityId);

    List<Booking> findFutureActiveBookingsByRoomId(Long roomId);

    void deleteById(Long id);

    BookingFullDto findByIdForUser(Long id, String userEmail);

    Booking findEntityByIdForUser(Long id, String userEmail);

    List<Booking> findBookingsForReviewRequest(BookingStatus status);

    void cancelByUser(Long id, String userEmail);

    BookingFullDto updateForUser(Long id, BookingUpdateDto bookingUpdateDto, String userEmail);

    BookingForReviewDto findForReview(Long id, String userEmail);

}
