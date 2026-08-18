package com.gmail.muha.booking.service.impl;

import com.gmail.muha.booking.dto.booking.BookingCreateDto;
import com.gmail.muha.booking.dto.booking.BookingUpdateDto;
import com.gmail.muha.booking.exception.InsufficientAmountOfMoneyInAccountException;
import com.gmail.muha.booking.exception.RoomsException;
import com.gmail.muha.booking.exception.WrongBookingDateException;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.Room;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import com.gmail.muha.booking.model.repository.RoomRepository;
import com.gmail.muha.booking.service.BookingPreparationService;
import com.gmail.muha.booking.service.impl.result.PreparedBookingData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingPreparationServiceImpl implements BookingPreparationService {

    private final RoomRepository roomRepository;

    @Override
    public PreparedBookingData prepare(BookingCreateDto bookingCreateDto, Hotel bookingHotel, User bookingUser) {

        checkBookingDates(bookingCreateDto.getStartDate(), bookingCreateDto.getEndDate());

        if (!roomRepository.existsRoomByHotelIdAndRoomTypeAndRoomCapacity(
                bookingHotel.getId(), bookingCreateDto.getRoomType(), bookingCreateDto.getRoomCapacity())) {
            throw new RoomsException("Hotel does not have rooms with specified criteria");
        }

        BigDecimal bookingCost = calculateBookingCost(
                bookingCreateDto.getStartDate(),
                bookingCreateDto.getEndDate(),
                bookingCreateDto.getRoomCapacity(),
                bookingCreateDto.getRoomType(),
                bookingHotel);

        checkSufficientBalance(bookingCost, bookingUser);

        List<Room> availableRooms = roomRepository.
                findSuitableActiveRooms(bookingHotel.getId(),
                        bookingCreateDto.getRoomType(),
                        bookingCreateDto.getRoomCapacity(),
                        bookingCreateDto.getStartDate(),
                        bookingCreateDto.getEndDate(),
                        BookingStatus.ACTIVE);

        if (availableRooms.isEmpty()) {
            throw new RoomsException("Unfortunately, there is no available suitable room for the specified dates");
        }
        return new PreparedBookingData(availableRooms.getFirst(), bookingCost);

    }

    @Override
    public PreparedBookingData prepareUpdate(
            BookingUpdateDto bookingUpdateDto, Hotel bookingHotel, User bookingUser, Booking updatingBooking) {

        fillMissingUpdateFields(bookingUpdateDto, updatingBooking);

        if (!roomRepository.existsRoomByHotelIdAndRoomTypeAndRoomCapacity(
                bookingHotel.getId(), bookingUpdateDto.getRoomType(), bookingUpdateDto.getRoomCapacity())) {
            throw new RoomsException("Hotel does not have rooms with specified criteria");
        }

        checkBookingDates(bookingUpdateDto.getStartDate(), bookingUpdateDto.getEndDate());

        BigDecimal bookingCost = calculateBookingCost(
                bookingUpdateDto.getStartDate(),
                bookingUpdateDto.getEndDate(),
                bookingUpdateDto.getRoomCapacity(),
                bookingUpdateDto.getRoomType(),
                bookingHotel);

        checkSufficientBalanceForUpdate(bookingCost, bookingUser, updatingBooking);

        List<Room> availableRooms = roomRepository.
                findSuitableActiveRoomsForUpdate(
                        bookingHotel.getId(),
                        bookingUpdateDto.getRoomType(),
                        bookingUpdateDto.getRoomCapacity(),
                        bookingUpdateDto.getStartDate(),
                        bookingUpdateDto.getEndDate(),
                        BookingStatus.ACTIVE,
                        updatingBooking.getId());

        if (availableRooms.isEmpty()) {
            throw new RoomsException("Unfortunately, there is no available suitable room for the specified dates");
        }
        return new PreparedBookingData(availableRooms.getFirst(), bookingCost);
    }

    private void checkBookingDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new WrongBookingDateException(
                    "Booking start date cannot be earlier than today"
            );
        }
        if (!endDate.isAfter(startDate)) {
            throw new WrongBookingDateException(
                    "Booking end date must be later than start date"
            );
        }
    }

    private void checkSufficientBalance(BigDecimal costBooking, User user) {
        BigDecimal userBalance = user.getBalance();
        if (userBalance.compareTo(costBooking) < 0) {
            throw new InsufficientAmountOfMoneyInAccountException("Insufficient funds to pay for this reservation");
        }
    }

    private void checkSufficientBalanceForUpdate(BigDecimal costBooking, User user, Booking booking) {
        BigDecimal userBalance = user.getBalance().add(booking.getTotalPrice());
        if (userBalance.compareTo(costBooking) < 0) {
            throw new InsufficientAmountOfMoneyInAccountException("Insufficient funds to pay for this reservation");
        }
    }

    private BigDecimal calculateBookingCost(LocalDate startDate,
                                            LocalDate endDate,
                                            RoomCapacity roomCapacity,
                                            RoomType roomType,
                                            Hotel hotel) {

        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);

        return hotel.getBasePricePerNight().multiply(BigDecimal.valueOf(numberOfDays))
                .multiply(BigDecimal.valueOf(roomCapacity.getCostFactor())
                        .multiply(BigDecimal.valueOf(roomType.getCostFactor())));

    }

    private void fillMissingUpdateFields(BookingUpdateDto bookingUpdateDto, Booking booking) {

        if (bookingUpdateDto.getRoomCapacity() == null) {
            bookingUpdateDto.setRoomCapacity(booking.getRoom().getRoomCapacity());
        }

        if (bookingUpdateDto.getRoomType() == null) {
            bookingUpdateDto.setRoomType(booking.getRoom().getRoomType());
        }

        if (bookingUpdateDto.getStartDate() == null) {
            bookingUpdateDto.setStartDate(booking.getStartDate());
        }

        if (bookingUpdateDto.getEndDate() == null) {
            bookingUpdateDto.setEndDate(booking.getEndDate());
        }
    }
}
