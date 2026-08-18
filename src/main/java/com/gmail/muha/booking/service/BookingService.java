package com.gmail.muha.booking.service;

import com.gmail.muha.booking.dto.booking.BookingCreateDto;
import com.gmail.muha.booking.dto.booking.BookingFullDto;
import com.gmail.muha.booking.dto.booking.BookingShortDto;
import com.gmail.muha.booking.dto.booking.BookingUpdateDto;
import com.gmail.muha.booking.model.entity.Booking;

import java.util.List;

public interface BookingService {

    BookingFullDto findById(Long id);

    Booking findEntityById(Long id);

    List<BookingShortDto> findAll();

    BookingFullDto create(BookingCreateDto bookingCreateDto);

    BookingFullDto update(Long id, BookingUpdateDto bookingUpdateDto);

    void completeExpiredBookings();

    List<Booking> findFutureActiveBookingsByUserId(Long userId);

    List<Booking> findFutureActiveBookingsByCityId(Long cityId);

    List<Booking> findFutureActiveBookingsByHotelId(Long cityId);

    List<Booking> findFutureActiveBookingsByRoomId(Long roomId);

    void deleteById(Long id);

}
