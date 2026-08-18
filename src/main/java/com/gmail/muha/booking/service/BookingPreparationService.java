package com.gmail.muha.booking.service;

import com.gmail.muha.booking.dto.booking.BookingCreateDto;
import com.gmail.muha.booking.dto.booking.BookingUpdateDto;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.service.impl.result.PreparedBookingData;

public interface BookingPreparationService {

    PreparedBookingData prepare(BookingCreateDto bookingCreateDto, Hotel hotel, User user);

    PreparedBookingData prepareUpdate(BookingUpdateDto bookingUpdateDto, Hotel hotel, User user, Booking booking);

}
