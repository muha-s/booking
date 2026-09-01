package com.gmail.muha.booking.service.booking.impl;

import com.gmail.muha.booking.exception.BookingCancellationException;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.Room;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingCancellationServiceImplTest {

    private final BookingCancellationServiceImpl bookingCancellationService =
            new BookingCancellationServiceImpl();

    @Test
    void shouldRefundFullAmountWhenCancelledByAdministration() {
        // given
        Booking booking = createBooking(
                5,
                BookingStatus.ACTIVE
        );

        // when
        bookingCancellationService.cancelByAdministration(booking);

        // then
        assertBigDecimalEquals(
                new BigDecimal("600.00"),
                booking.getUser().getBalance()
        );

        assertBigDecimalEquals(
                new BigDecimal("900.00"),
                booking.getRoom().getHotel().getBalance()
        );

        assertEquals(
                BookingStatus.CANCELLED,
                booking.getStatus()
        );
    }

    @Test
    void shouldRefundFullAmountWhenCancelledMoreThanSevenDaysBeforeCheckIn() {
        // given
        Booking booking = createBooking(
                8,
                BookingStatus.ACTIVE
        );

        // when
        bookingCancellationService.cancelByUser(booking);

        // then
        assertBigDecimalEquals(
                new BigDecimal("600.00"),
                booking.getUser().getBalance()
        );

        assertBigDecimalEquals(
                new BigDecimal("900.00"),
                booking.getRoom().getHotel().getBalance()
        );

        assertEquals(
                BookingStatus.CANCELLED,
                booking.getStatus()
        );
    }

    @Test
    void shouldRefundSeventyFivePercentWhenCancelledFourToSevenDaysBeforeCheckIn() {
        // given
        Booking booking = createBooking(
                5,
                BookingStatus.ACTIVE
        );

        // when
        bookingCancellationService.cancelByUser(booking);

        // then
        assertBigDecimalEquals(
                new BigDecimal("575.00"),
                booking.getUser().getBalance()
        );

        assertBigDecimalEquals(
                new BigDecimal("925.00"),
                booking.getRoom().getHotel().getBalance()
        );

        assertEquals(
                BookingStatus.CANCELLED,
                booking.getStatus()
        );
    }

    @Test
    void shouldRefundFiftyPercentWhenCancelledOneToThreeDaysBeforeCheckIn() {
        // given
        Booking booking = createBooking(
                2,
                BookingStatus.ACTIVE
        );

        // when
        bookingCancellationService.cancelByUser(booking);

        // then
        assertBigDecimalEquals(
                new BigDecimal("550.00"),
                booking.getUser().getBalance()
        );

        assertBigDecimalEquals(
                new BigDecimal("950.00"),
                booking.getRoom().getHotel().getBalance()
        );

        assertEquals(
                BookingStatus.CANCELLED,
                booking.getStatus()
        );
    }

    @Test
    void shouldRejectCancellationWhenBookingIsNotActive() {
        // given
        Booking booking = createBooking(
                5,
                BookingStatus.COMPLETED
        );

        // when / then
        assertThrows(
                BookingCancellationException.class,
                () -> bookingCancellationService.cancelByUser(booking)
        );

        assertBigDecimalEquals(
                new BigDecimal("500.00"),
                booking.getUser().getBalance()
        );

        assertBigDecimalEquals(
                new BigDecimal("1000.00"),
                booking.getRoom().getHotel().getBalance()
        );

        assertEquals(
                BookingStatus.COMPLETED,
                booking.getStatus()
        );
    }

    @Test
    void shouldRejectCancellationWhenCheckInDateIsInPast() {
        // given
        Booking booking = createBooking(
                -1,
                BookingStatus.ACTIVE
        );

        // when / then
        assertThrows(
                BookingCancellationException.class,
                () -> bookingCancellationService.cancelByUser(booking)
        );

        assertBigDecimalEquals(
                new BigDecimal("500.00"),
                booking.getUser().getBalance()
        );

        assertBigDecimalEquals(
                new BigDecimal("1000.00"),
                booking.getRoom().getHotel().getBalance()
        );

        assertEquals(
                BookingStatus.ACTIVE,
                booking.getStatus()
        );
    }

    private Booking createBooking(
            long daysBeforeCheckIn,
            BookingStatus status
    ) {
        User user = new User();
        user.setBalance(new BigDecimal("500.00"));

        Hotel hotel = new Hotel();
        hotel.setBalance(new BigDecimal("1000.00"));

        Room room = new Room();
        room.setHotel(hotel);

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setRoom(room);
        booking.setStartDate(
                LocalDate.now().plusDays(daysBeforeCheckIn)
        );
        booking.setEndDate(
                LocalDate.now().plusDays(daysBeforeCheckIn + 3)
        );
        booking.setStatus(status);
        booking.setTotalPrice(new BigDecimal("100.00"));

        return booking;
    }

    private void assertBigDecimalEquals(
            BigDecimal expected,
            BigDecimal actual
    ) {
        assertEquals(0, expected.compareTo(actual));
    }
}