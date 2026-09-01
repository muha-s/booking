package com.gmail.muha.booking.service.booking.impl;

import com.gmail.muha.booking.dto.booking.BookingCreateDto;
import com.gmail.muha.booking.dto.booking.BookingUpdateDto;
import com.gmail.muha.booking.exception.InsufficientAmountOfMoneyInAccountException;
import com.gmail.muha.booking.exception.RoomsException;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.Room;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import com.gmail.muha.booking.model.repository.RoomRepository;
import com.gmail.muha.booking.service.booking.result.PreparedBookingData;
import com.gmail.muha.booking.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingPreparationServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private BookingPreparationServiceImpl bookingPreparationService;

    @Test
    void shouldPrepareBooking() {
        // given
        Hotel hotel = createHotel();
        User user = createUser(new BigDecimal("5000.00"));
        Room room = createRoom(hotel);

        BookingCreateDto dto = createBookingCreateDto();

        when(roomRepository.existsRoomByHotelIdAndRoomTypeAndRoomCapacity(
                hotel.getId(),
                dto.getRoomType(),
                dto.getRoomCapacity()
        )).thenReturn(true);

        when(roomRepository.findSuitableActiveRooms(
                hotel.getId(),
                dto.getRoomType(),
                dto.getRoomCapacity(),
                dto.getStartDate(),
                dto.getEndDate(),
                BookingStatus.ACTIVE
        )).thenReturn(List.of(room));

        // when
        PreparedBookingData result =
                bookingPreparationService.prepare(dto, hotel, user);

        // then
        assertSame(room, result.room());
        assertBigDecimalEquals(
                calculateExpectedPrice(dto, hotel),
                result.totalPrice()
        );

        verify(validator).validateBookingDates(
                dto.getStartDate(),
                dto.getEndDate()
        );
    }

    @Test
    void shouldThrowExceptionWhenHotelDoesNotHaveSuitableRoomType() {
        // given
        Hotel hotel = createHotel();
        User user = createUser(new BigDecimal("5000.00"));
        BookingCreateDto dto = createBookingCreateDto();

        when(roomRepository.existsRoomByHotelIdAndRoomTypeAndRoomCapacity(
                hotel.getId(),
                dto.getRoomType(),
                dto.getRoomCapacity()
        )).thenReturn(false);

        // when / then
        assertThrows(
                RoomsException.class,
                () -> bookingPreparationService.prepare(
                        dto,
                        hotel,
                        user
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenUserHasInsufficientBalance() {
        // given
        Hotel hotel = createHotel();
        User user = createUser(BigDecimal.ZERO);
        BookingCreateDto dto = createBookingCreateDto();

        when(roomRepository.existsRoomByHotelIdAndRoomTypeAndRoomCapacity(
                hotel.getId(),
                dto.getRoomType(),
                dto.getRoomCapacity()
        )).thenReturn(true);

        // when / then
        assertThrows(
                InsufficientAmountOfMoneyInAccountException.class,
                () -> bookingPreparationService.prepare(
                        dto,
                        hotel,
                        user
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenNoRoomIsAvailableForDates() {
        // given
        Hotel hotel = createHotel();
        User user = createUser(new BigDecimal("5000.00"));
        BookingCreateDto dto = createBookingCreateDto();

        when(roomRepository.existsRoomByHotelIdAndRoomTypeAndRoomCapacity(
                hotel.getId(),
                dto.getRoomType(),
                dto.getRoomCapacity()
        )).thenReturn(true);

        when(roomRepository.findSuitableActiveRooms(
                hotel.getId(),
                dto.getRoomType(),
                dto.getRoomCapacity(),
                dto.getStartDate(),
                dto.getEndDate(),
                BookingStatus.ACTIVE
        )).thenReturn(List.of());

        // when / then
        assertThrows(
                RoomsException.class,
                () -> bookingPreparationService.prepare(
                        dto,
                        hotel,
                        user
                )
        );
    }

    @Test
    void shouldPrepareBookingUpdateAndFillMissingFields() {
        // given
        Hotel hotel = createHotel();
        User user = createUser(new BigDecimal("5000.00"));

        Room currentRoom = createRoom(hotel);
        Room selectedRoom = createRoom(hotel);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setRoom(currentRoom);
        booking.setStartDate(LocalDate.now().plusDays(10));
        booking.setEndDate(LocalDate.now().plusDays(13));
        booking.setTotalPrice(new BigDecimal("300.00"));

        BookingUpdateDto dto = new BookingUpdateDto();

        when(roomRepository.existsRoomByHotelIdAndRoomTypeAndRoomCapacity(
                hotel.getId(),
                currentRoom.getRoomType(),
                currentRoom.getRoomCapacity()
        )).thenReturn(true);

        when(roomRepository.findSuitableActiveRoomsForUpdate(
                hotel.getId(),
                currentRoom.getRoomType(),
                currentRoom.getRoomCapacity(),
                booking.getStartDate(),
                booking.getEndDate(),
                BookingStatus.ACTIVE,
                booking.getId()
        )).thenReturn(List.of(selectedRoom));

        // when
        PreparedBookingData result =
                bookingPreparationService.prepareUpdate(
                        dto,
                        hotel,
                        user,
                        booking
                );

        // then
        assertEquals(currentRoom.getRoomType(), dto.getRoomType());
        assertEquals(currentRoom.getRoomCapacity(), dto.getRoomCapacity());
        assertEquals(booking.getStartDate(), dto.getStartDate());
        assertEquals(booking.getEndDate(), dto.getEndDate());

        assertSame(selectedRoom, result.room());

        verify(validator).validateBookingDates(
                booking.getStartDate(),
                booking.getEndDate()
        );
    }

    @Test
    void shouldIncludeOldBookingPriceWhenCheckingBalanceForUpdate() {
        // given
        Hotel hotel = createHotel();

        Room currentRoom = createRoom(hotel);
        Room selectedRoom = createRoom(hotel);

        Booking booking = new Booking();
        booking.setId(20L);
        booking.setRoom(currentRoom);
        booking.setStartDate(LocalDate.now().plusDays(10));
        booking.setEndDate(LocalDate.now().plusDays(11));
        booking.setTotalPrice(new BigDecimal("100.00"));

        User user = createUser(BigDecimal.ZERO);

        BookingUpdateDto dto = new BookingUpdateDto();
        dto.setRoomType(currentRoom.getRoomType());
        dto.setRoomCapacity(currentRoom.getRoomCapacity());
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());

        when(roomRepository.existsRoomByHotelIdAndRoomTypeAndRoomCapacity(
                hotel.getId(),
                dto.getRoomType(),
                dto.getRoomCapacity()
        )).thenReturn(true);

        when(roomRepository.findSuitableActiveRoomsForUpdate(
                hotel.getId(),
                dto.getRoomType(),
                dto.getRoomCapacity(),
                dto.getStartDate(),
                dto.getEndDate(),
                BookingStatus.ACTIVE,
                booking.getId()
        )).thenReturn(List.of(selectedRoom));

        // when
        PreparedBookingData result =
                bookingPreparationService.prepareUpdate(
                        dto,
                        hotel,
                        user,
                        booking
                );

        // then
        assertSame(selectedRoom, result.room());
        assertBigDecimalEquals(
                new BigDecimal("100.00"),
                result.totalPrice()
        );
    }

    private BookingCreateDto createBookingCreateDto() {
        BookingCreateDto dto = new BookingCreateDto();

        dto.setRoomType(RoomType.values()[0]);
        dto.setRoomCapacity(RoomCapacity.values()[0]);
        dto.setStartDate(LocalDate.now().plusDays(10));
        dto.setEndDate(LocalDate.now().plusDays(13));

        return dto;
    }

    private Hotel createHotel() {
        Hotel hotel = new Hotel();

        hotel.setId(1L);
        hotel.setBasePricePerNight(new BigDecimal("100.00"));

        return hotel;
    }

    private User createUser(BigDecimal balance) {
        User user = new User();
        user.setBalance(balance);

        return user;
    }

    private Room createRoom(Hotel hotel) {
        Room room = new Room();

        room.setHotel(hotel);
        room.setRoomType(RoomType.values()[0]);
        room.setRoomCapacity(RoomCapacity.values()[0]);

        return room;
    }

    private BigDecimal calculateExpectedPrice(
            BookingCreateDto dto,
            Hotel hotel
    ) {
        long numberOfDays = ChronoUnit.DAYS.between(
                dto.getStartDate(),
                dto.getEndDate()
        );

        return hotel.getBasePricePerNight()
                .multiply(BigDecimal.valueOf(numberOfDays))
                .multiply(
                        BigDecimal.valueOf(dto.getRoomCapacity().getCostFactor())
                                .multiply(
                                        BigDecimal.valueOf(
                                                dto.getRoomType().getCostFactor()
                                        )
                                )
                );
    }

    private void assertBigDecimalEquals(
            BigDecimal expected,
            BigDecimal actual
    ) {
        assertEquals(0, expected.compareTo(actual));
    }
}