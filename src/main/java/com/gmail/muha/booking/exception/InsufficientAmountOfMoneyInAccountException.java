package com.gmail.muha.booking.exception;

public class InsufficientAmountOfMoneyInAccountException extends  RuntimeException{

    public InsufficientAmountOfMoneyInAccountException(String message) {
        super(message);
    }
}
