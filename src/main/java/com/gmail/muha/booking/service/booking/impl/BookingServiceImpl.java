package com.gmail.muha.booking.service.booking.impl;

import com.gmail.muha.booking.dto.booking.*;
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
import com.gmail.muha.booking.service.booking.BookingService;
import com.gmail.muha.booking.service.booking.result.PreparedBookingData;
import com.gmail.muha.booking.service.hotel.HotelService;
import com.gmail.muha.booking.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final HotelService hotelService;
    private final BookingPreparationService bookingPreparationService;
    private final BookingCancellationService bookingCancellationService;


    @Override
    public Page<BookingForUserDto> findAllByUserEmail(String userEmail, Pageable pageable) {
        return bookingRepository.findAllByUserEmail(userEmail, pageable).map(bookingMapper::toForUserDto);
    }

    @Override
    public Page<BookingManagedDto> findManagedByHotelId(Long hotelId, Pageable pageable) {
        return bookingRepository.findAllByHotelId(hotelId, pageable).map(bookingMapper::toManagedDto);
    }

    @Override
    public BookingForReviewDto findForReview(Long id, String userEmail) {
        Booking booking = findEntityByIdForUser(id, userEmail);
        return bookingMapper.toForReviewDto(booking);
    }

    @Transactional
    @Override
    public void create(BookingCreateDto bookingCreateDto, String userEmail) {

        User bookingUser = userService.findEntityByEmail(userEmail);
        Hotel bookingHotel = hotelService.findEntityById(bookingCreateDto.getHotelId());

        PreparedBookingData preparedBookingData =
                bookingPreparationService.prepare(bookingCreateDto, bookingHotel, bookingUser);

        Room selectedRoom = preparedBookingData.room();
        BigDecimal totalPrice = preparedBookingData.totalPrice();

        Booking creatingBooking = bookingMapper.toEntity(bookingCreateDto, bookingUser, selectedRoom);

        creatingBooking.setTotalPrice(totalPrice);

        bookingUser.setBalance(bookingUser.getBalance().subtract(totalPrice));
        bookingHotel.setBalance(bookingHotel.getBalance().add(totalPrice));
        bookingRepository.save(creatingBooking);
    }

    @Override
    public Booking findEntityByIdForUser(Long id, String userEmail) {
        return bookingRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new NotFoundException("Booking was not found"));
    }

    @Override
    public List<Booking> findBookingsForReviewRequest(BookingStatus status) {
        return bookingRepository.findBookingsForReviewRequest(status);
    }

    @Transactional
    @Override
    public void cancelByUser(Long id, String userEmail) {
        Booking booking = findEntityByIdForUser(id, userEmail);
        bookingCancellationService.cancelByUser(booking);
    }

    @Transactional
    @Override
    public void updateForUser(Long id, BookingUpdateDto bookingUpdateDto, String userEmail) {

        Booking updatingBooking = findEntityByIdForUser(id, userEmail);

        if (updatingBooking.getStatus() != BookingStatus.ACTIVE) {
            log.warn("Booking update rejected: bookingId={}, status={}",
                    updatingBooking.getId(),
                    updatingBooking.getStatus());
            throw new BookingUpdateException("Only active bookings can be updated");
        }

        User bookingUser = updatingBooking.getUser();
        Hotel bookingHotel = updatingBooking.getRoom().getHotel();

        PreparedBookingData preparedBookingData =
                bookingPreparationService.prepareUpdate(bookingUpdateDto, bookingHotel, bookingUser, updatingBooking);

        Room selectedRoom = preparedBookingData.room();
        BigDecimal oldPrice = updatingBooking.getTotalPrice();
        BigDecimal newPrice = preparedBookingData.totalPrice();

        bookingUser.setBalance(bookingUser.getBalance().add(oldPrice).subtract(newPrice));
        updatingBooking.setTotalPrice(newPrice);
        bookingHotel.setBalance(bookingHotel.getBalance().subtract(oldPrice).add(newPrice));

        bookingMapper.updateEntity(bookingUpdateDto, updatingBooking, selectedRoom);
    }

    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void completeExpiredBookings() {
        List<Booking> expiredBookings =
                bookingRepository.findExpiredBookings(BookingStatus.ACTIVE, LocalDate.now());

        expiredBookings.forEach(booking -> booking.setStatus(BookingStatus.COMPLETED));
    }
}