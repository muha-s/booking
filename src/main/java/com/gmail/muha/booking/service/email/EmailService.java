package com.gmail.muha.booking.service.email;

public interface EmailService {

    void sendEmail(String to, String subject, String text);
}