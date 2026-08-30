package com.gmail.muha.booking.service.email.impl;

import com.gmail.muha.booking.exception.EmailSendingException;
import com.gmail.muha.booking.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
        } catch (Exception exception) {
            throw new EmailSendingException(
                    "Failed to send email to: " + to,
                    exception
            );
        }
    }
}