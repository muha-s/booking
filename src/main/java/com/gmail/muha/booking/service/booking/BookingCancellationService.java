package com.gmail.muha.booking.service.booking;

import com.gmail.muha.booking.model.entity.Booking;

public interface BookingCancellationService {

    void cancelByAdministration(Booking cancellingBooking);

    void cancelByUser(Booking cancellingBooking);

}
