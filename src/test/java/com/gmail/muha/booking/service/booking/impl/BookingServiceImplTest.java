package com.gmail.muha.booking.service.booking.impl;

import com.gmail.muha.booking.dto.booking.BookingCreateDto;
import com.gmail.muha.booking.dto.booking.BookingForReviewDto;
import com.gmail.muha.booking.dto.booking.BookingForUserDto;
import com.gmail.muha.booking.dto.booking.BookingManagedDto;
import com.gmail.muha.booking.dto.booking.BookingUpdateDto;
import com.gmail.muha.booking.exception.BookingUpdateException;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.BookingMapper;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.Room;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import com.gmail.muha.booking.model.repository.BookingRepository;
import com.gmail.muha.booking.service.booking.BookingCancellationService;
import com.gmail.muha.booking.service.booking.BookingPreparationService;
import com.gmail.muha.booking.service.booking.result.PreparedBookingData;
import com.gmail.muha.booking.service.hotel.HotelService;
import com.gmail.muha.booking.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private UserService userService;

    @Mock
    private HotelService hotelService;

    @Mock
    private BookingPreparationService bookingPreparationService;

    @Mock
    private BookingCancellationService bookingCancellationService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void shouldFindAllBookingsByUserEmail() {
        String email = "user@test.com";
        Pageable pageable = PageRequest.of(0, 10);

        Booking booking = new Booking();
        BookingForUserDto dto = new BookingForUserDto();

        when(bookingRepository.findAllByUserEmail(email, pageable))
                .thenReturn(new PageImpl<>(List.of(booking)));

        when(bookingMapper.toForUserDto(booking)).thenReturn(dto);

        Page<BookingForUserDto> result =
                bookingService.findAllByUserEmail(email, pageable);

        assertEquals(1, result.getTotalElements());
        assertSame(dto, result.getContent().getFirst());

        verify(bookingRepository).findAllByUserEmail(email, pageable);
        verify(bookingMapper).toForUserDto(booking);
    }

    @Test
    void shouldFindManagedBookingsByHotelId() {
        Long hotelId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Booking booking = new Booking();
        BookingManagedDto dto = new BookingManagedDto();

        when(bookingRepository.findAllByHotelId(hotelId, pageable))
                .thenReturn(new PageImpl<>(List.of(booking)));

        when(bookingMapper.toManagedDto(booking)).thenReturn(dto);

        Page<BookingManagedDto> result =
                bookingService.findManagedByHotelId(hotelId, pageable);

        assertEquals(1, result.getTotalElements());
        assertSame(dto, result.getContent().getFirst());

        verify(bookingRepository).findAllByHotelId(hotelId, pageable);
        verify(bookingMapper).toManagedDto(booking);
    }

    @Test
    void shouldFindBookingForReview() {
        Long bookingId = 1L;
        String email = "user@test.com";

        Booking booking = new Booking();
        BookingForReviewDto dto = new BookingForReviewDto();

        when(bookingRepository.findByIdAndUserEmail(bookingId, email))
                .thenReturn(Optional.of(booking));

        when(bookingMapper.toForReviewDto(booking)).thenReturn(dto);

        BookingForReviewDto result =
                bookingService.findForReview(bookingId, email);

        assertSame(dto, result);

        verify(bookingRepository).findByIdAndUserEmail(bookingId, email);
        verify(bookingMapper).toForReviewDto(booking);
    }

    @Test
    void shouldCreateBookingAndUpdateBalances() {
        String email = "user@test.com";

        BookingCreateDto dto = new BookingCreateDto();
        dto.setHotelId(1L);

        User user = new User();
        user.setBalance(new BigDecimal("1000.00"));

        Hotel hotel = new Hotel();
        hotel.setBalance(new BigDecimal("500.00"));

        Room room = new Room();

        BigDecimal totalPrice = new BigDecimal("200.00");

        PreparedBookingData preparedBookingData =
                new PreparedBookingData(room, totalPrice);

        Booking booking = new Booking();

        when(userService.findEntityByEmail(email)).thenReturn(user);
        when(hotelService.findEntityById(1L)).thenReturn(hotel);

        when(bookingPreparationService.prepare(dto, hotel, user))
                .thenReturn(preparedBookingData);

        when(bookingMapper.toEntity(dto, user, room)).thenReturn(booking);

        bookingService.create(dto, email);

        assertEquals(new BigDecimal("800.00"), user.getBalance());
        assertEquals(new BigDecimal("700.00"), hotel.getBalance());
        assertEquals(new BigDecimal("200.00"), booking.getTotalPrice());

        verify(bookingRepository).save(booking);
    }

    @Test
    void shouldThrowExceptionWhenBookingWasNotFound() {
        Long bookingId = 1L;
        String email = "user@test.com";

        when(bookingRepository.findByIdAndUserEmail(bookingId, email))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> bookingService.findEntityByIdForUser(bookingId, email)
        );
    }

    @Test
    void shouldCancelBookingByUser() {
        Long bookingId = 1L;
        String email = "user@test.com";

        Booking booking = new Booking();

        when(bookingRepository.findByIdAndUserEmail(bookingId, email))
                .thenReturn(Optional.of(booking));

        bookingService.cancelByUser(bookingId, email);

        verify(bookingCancellationService).cancelByUser(booking);
    }

    @Test
    void shouldRejectUpdateForNonActiveBooking() {
        Long bookingId = 1L;
        String email = "user@test.com";

        BookingUpdateDto dto = new BookingUpdateDto();

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStatus(BookingStatus.CANCELLED);

        when(bookingRepository.findByIdAndUserEmail(bookingId, email))
                .thenReturn(Optional.of(booking));

        assertThrows(
                BookingUpdateException.class,
                () -> bookingService.updateForUser(bookingId, dto, email)
        );

        verify(bookingPreparationService, never())
                .prepareUpdate(
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any()
                );
    }

    @Test
    void shouldUpdateActiveBookingAndBalances() {
        Long bookingId = 1L;
        String email = "user@test.com";

        BookingUpdateDto dto = new BookingUpdateDto();

        User user = new User();
        user.setBalance(new BigDecimal("500.00"));

        Hotel hotel = new Hotel();
        hotel.setBalance(new BigDecimal("1000.00"));

        Room oldRoom = new Room();
        oldRoom.setHotel(hotel);

        Room newRoom = new Room();

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStatus(BookingStatus.ACTIVE);
        booking.setUser(user);
        booking.setRoom(oldRoom);
        booking.setTotalPrice(new BigDecimal("200.00"));

        PreparedBookingData preparedBookingData =
                new PreparedBookingData(
                        newRoom,
                        new BigDecimal("300.00")
                );

        when(bookingRepository.findByIdAndUserEmail(bookingId, email))
                .thenReturn(Optional.of(booking));

        when(bookingPreparationService.prepareUpdate(dto, hotel, user, booking))
                .thenReturn(preparedBookingData);

        bookingService.updateForUser(bookingId, dto, email);

        assertEquals(new BigDecimal("400.00"), user.getBalance());
        assertEquals(new BigDecimal("1100.00"), hotel.getBalance());
        assertEquals(new BigDecimal("300.00"), booking.getTotalPrice());

        verify(bookingMapper).updateEntity(dto, booking, newRoom);
    }

    @Test
    void shouldFindBookingsForReviewRequest() {
        Booking firstBooking = new Booking();
        Booking secondBooking = new Booking();

        List<Booking> bookings =
                List.of(firstBooking, secondBooking);

        when(bookingRepository.findBookingsForReviewRequest(BookingStatus.COMPLETED))
                .thenReturn(bookings);

        List<Booking> result =
                bookingService.findBookingsForReviewRequest(BookingStatus.COMPLETED);

        assertSame(bookings, result);
    }

    @Test
    void shouldCompleteExpiredBookings() {
        Booking firstBooking = new Booking();
        firstBooking.setStatus(BookingStatus.ACTIVE);

        Booking secondBooking = new Booking();
        secondBooking.setStatus(BookingStatus.ACTIVE);

        when(bookingRepository.findExpiredBookings(
                BookingStatus.ACTIVE,
                LocalDate.now()
        )).thenReturn(List.of(firstBooking, secondBooking));

        bookingService.completeExpiredBookings();

        assertEquals(BookingStatus.COMPLETED, firstBooking.getStatus());
        assertEquals(BookingStatus.COMPLETED, secondBooking.getStatus());
    }
}