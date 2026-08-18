package com.gmail.muha.booking.exception;

public class WrongBookingDateException extends RuntimeException {
    public WrongBookingDateException(String message) {
        super(message);
    }
}
