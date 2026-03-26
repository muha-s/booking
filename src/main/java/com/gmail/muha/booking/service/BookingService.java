package com.gmail.muha.booking.service;

import com.gmail.muha.booking.dto.booking_dto.BookingCreateDto;
import com.gmail.muha.booking.dto.booking_dto.BookingResponseDto;
import com.gmail.muha.booking.dto.booking_dto.BookingUpdateDto;
import com.gmail.muha.booking.entity.Booking;

import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(BookingCreateDto dto);

    BookingResponseDto getBooking(Long id);

    List<BookingResponseDto> getAllBookings();

    void deleteBooking(Long id);

    List<BookingResponseDto> findByUserId(Long userId);

    List<BookingResponseDto> findByRoomId(Long roomId);

    Booking findBookingEntityById(Long id);

    BookingResponseDto updateBooking(BookingUpdateDto bookingUpdateDto);

}
