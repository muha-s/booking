package com.gmail.muha.booking.exception;

public class AlreadyBookedException extends RuntimeException{

    public AlreadyBookedException(String message) {
        super(message);
    }
}
