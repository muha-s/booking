package com.gmail.muha.booking.service.booking.impl;

import com.gmail.muha.booking.exception.BookingCancellationException;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import com.gmail.muha.booking.service.booking.BookingCancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BookingCancellationServiceImpl implements BookingCancellationService {

    private static final LocalTime CHECK_IN_TIME = LocalTime.of(12, 0, 0);

    private static final long THREE_DAYS_BEFORE_CHECK_IN = 3;
    private static final long SEVEN_DAYS_BEFORE_CHECK_IN = 7;

    private static final BigDecimal REFUND_RATE_25 = BigDecimal.valueOf(0.25);
    private static final BigDecimal REFUND_RATE_50 = BigDecimal.valueOf(0.50);
    private static final BigDecimal REFUND_RATE_75 = BigDecimal.valueOf(0.75);
    private static final BigDecimal REFUND_RATE_100 = BigDecimal.ONE;


    @Override
    public void cancelByAdministration(Booking booking) {
        processRefund(booking, booking.getTotalPrice());
    }

    @Override
    public void cancelByUser(Booking booking) {
        BigDecimal refundAmount = calculateRefundAmount(booking);
        processRefund(booking, refundAmount);
    }


    private BigDecimal calculateRefundAmount(Booking booking) {
        validateCancellation(booking);

        BigDecimal refundRate = calculateRefundRate(booking);
        return booking.getTotalPrice().multiply(refundRate);
    }

    private void validateCancellation(Booking booking) {

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new BookingCancellationException("Only active bookings can be cancelled."
            );
        }
        if (booking.getStartDate().isBefore(LocalDate.now())) {
            throw new BookingCancellationException("It is no longer possible to cancel the reservation.");
        }
        if (booking.getStartDate().equals(LocalDate.now()) && LocalTime.now().isAfter(CHECK_IN_TIME)) {
            throw new BookingCancellationException("It is no longer possible to cancel the reservation.");
        }
    }

    private BigDecimal calculateRefundRate(Booking booking) {
        long daysBeforeCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), booking.getStartDate());

        if (daysBeforeCheckIn == 0) {
            return REFUND_RATE_25;
        } else if (daysBeforeCheckIn <= THREE_DAYS_BEFORE_CHECK_IN) {
            return REFUND_RATE_50;
        } else if (daysBeforeCheckIn <= SEVEN_DAYS_BEFORE_CHECK_IN) {
            return REFUND_RATE_75;
        } else {
            return REFUND_RATE_100;
        }
    }

    private void processRefund(Booking booking, BigDecimal refundAmount) {
        User user = booking.getUser();
        Hotel hotel = booking.getRoom().getHotel();

        hotel.setBalance(hotel.getBalance().subtract(refundAmount));
        user.setBalance(user.getBalance().add(refundAmount));
        booking.setStatus(BookingStatus.CANCELLED);
    }
}
