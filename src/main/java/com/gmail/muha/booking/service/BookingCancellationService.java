package com.gmail.muha.booking.service;

import com.gmail.muha.booking.model.entity.Booking;

import java.math.BigDecimal;

public interface BookingCancellationService {

    void cancelByAdministration(Booking cancellingBooking);

    void cancelByUser(Booking cancellingBooking);

}
