package com.gmail.muha.booking.service.impl;

import com.gmail.muha.booking.dto.booking.BookingCreateDto;
import com.gmail.muha.booking.dto.booking.BookingFullDto;
import com.gmail.muha.booking.dto.booking.BookingShortDto;
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
import com.gmail.muha.booking.service.*;
import com.gmail.muha.booking.service.impl.result.PreparedBookingData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.gmail.muha.booking.model.entity.enums.BookingStatus.ACTIVE;

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
    public BookingFullDto findById(Long id) {
        return bookingMapper.toFullDto(findEntityById(id));
    }

    @Override
    public Booking findEntityById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Booking was not found by id: " + id));
    }

    @Override
    public List<BookingShortDto> findAll() {
        return bookingMapper.toShortDtoList(bookingRepository.findAll());
    }

    @Transactional
    @Override
    public BookingFullDto create(BookingCreateDto bookingCreateDto) {

        User bookingUser = userService.findEntityById(bookingCreateDto.getUserId());
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
        return bookingMapper.toFullDto(creatingBooking);
    }

    @Transactional
    @Override
    public BookingFullDto update(Long id, BookingUpdateDto bookingUpdateDto) {

        Booking updatingBooking = findEntityById(id);

        if(updatingBooking.getStatus() != ACTIVE){
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

        return bookingMapper.toFullDto(updatingBooking);
    }

    @Override
    public List<Booking> findFutureActiveBookingsByUserId(Long userId) {
        return bookingRepository.findFutureActiveBookingsByUserId(userId);

    }

    @Override
    public List<Booking> findFutureActiveBookingsByCityId(Long cityId) {
        return bookingRepository.findFutureActiveBookingsByCityId(cityId);
    }

    @Override
    public List<Booking> findFutureActiveBookingsByHotelId(Long hotelId) {
        return bookingRepository.findFutureActiveBookingsByHotelId(hotelId);
    }

    @Override
    public List<Booking> findFutureActiveBookingsByRoomId(Long roomId) {
        return bookingRepository.findFutureActiveBookingsByRoomId(roomId);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Booking booking = findEntityById(id);
        bookingCancellationService.cancelByUser(booking);
    }

    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    @Override
    public void completeExpiredBookings() {
        List<Booking> expiredBookings = bookingRepository.findExpiredBookings(ACTIVE, LocalDate.now());

        expiredBookings.forEach(booking -> booking.setStatus(BookingStatus.COMPLETED));
    }
}
