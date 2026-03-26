package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.booking_dto.BookingCreateDto;
import com.gmail.muha.booking.dto.booking_dto.BookingResponseDto;
import com.gmail.muha.booking.dto.booking_dto.BookingUpdateDto;
import com.gmail.muha.booking.service.BookingService;

import java.sql.Date;
import java.util.List;

public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    public BookingResponseDto createBooking(Long userId, Long roomId, Date startDate, Date endDate) {
        BookingCreateDto createBookingDto = new BookingCreateDto(userId, roomId, startDate, endDate);
        return service.createBooking(createBookingDto);
    }

    public BookingResponseDto getBooking(Long id) {
        return service.getBooking(id);
    }

    public List<BookingResponseDto> getBookings() {
        return service.getAllBookings();
    }

    public BookingResponseDto updateBooking(Long id, Long roomId, Date startDate, Date endDate) {
        BookingUpdateDto updateBookingDto = new BookingUpdateDto(id, roomId, startDate, endDate);
        return service.updateBooking(updateBookingDto);

    }

    public void deleteBooking(Long id) {
        service.deleteBooking(id);
    }

    public List<BookingResponseDto> findByUserId(Long userId) {
        return service.findByUserId(userId);
    }

    public List<BookingResponseDto> findByRoomId(Long roomId) {
        return service.findByRoomId(roomId);
    }
}
