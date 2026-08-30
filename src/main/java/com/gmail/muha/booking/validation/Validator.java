package com.gmail.muha.booking.validation;

import com.gmail.muha.booking.exception.WrongBookingDateException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Validator {


    public void validateBookingDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new WrongBookingDateException(
                    "Booking start date cannot be earlier than today"
            );
        }
        if (!endDate.isAfter(startDate)) {
            throw new WrongBookingDateException(
                    "Booking end date must be later than start date"
            );
        }
    }
}
