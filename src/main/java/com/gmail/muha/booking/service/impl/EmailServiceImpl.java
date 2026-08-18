package com.gmail.muha.booking.service.impl;

import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import com.gmail.muha.booking.model.repository.BookingRepository;
import com.gmail.muha.booking.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final static BookingStatus STATUS_FOR_REVIEW_REQUEST = BookingStatus.COMPLETED;

    private final JavaMailSender mailSender;
    private final BookingRepository bookingRepository;


    @Scheduled(cron = "0 0 13 * * *")
    @Transactional
    @Override
    public void sendReviewRequests() {

        List<Booking> bookingsForReviewRequest =
                bookingRepository.findBookingsForReviewRequest(STATUS_FOR_REVIEW_REQUEST);

        bookingsForReviewRequest.forEach(this::sendReviewRequestEmail);

    }

    private void sendReviewRequestEmail(Booking booking) {

        String recipientEmail = booking.getUser().getEmail();
        String hotelName = booking.getRoom().getHotel().getName();

        String reviewUrl = "http://localhost:8080/reviews/booking/" + booking.getId();

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipientEmail);
        message.setSubject("Please rate your stay at " + hotelName);
        message.setText("""
                Thank you for staying at %s.
                We would appreciate it if you could leave a review.
                %s
                """.formatted(hotelName, reviewUrl));
        mailSender.send(message);
        booking.setReviewRequestSentAt(Instant.now());
    }

    @Override
    public void sendTestEmail() {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("rudenkoksenia13@gmail.com");
        message.setSubject("Не забывай убираться в комнате!!!!!!!");
        message.setText("Email sending works.");

        mailSender.send(message);
    }
}
