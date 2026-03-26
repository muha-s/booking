package com.gmail.muha.booking.service.impl;

import com.gmail.muha.booking.dto.booking_dto.BookingCreateDto;
import com.gmail.muha.booking.dto.booking_dto.BookingResponseDto;
import com.gmail.muha.booking.dto.booking_dto.BookingUpdateDto;
import com.gmail.muha.booking.entity.Booking;
import com.gmail.muha.booking.entity.Hotel;
import com.gmail.muha.booking.entity.Room;
import com.gmail.muha.booking.entity.User;
import com.gmail.muha.booking.exception.AlreadyBookedException;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.BookingMapper;
import com.gmail.muha.booking.repository.BookingRepository;
import com.gmail.muha.booking.service.BookingService;
import com.gmail.muha.booking.service.HotelService;
import com.gmail.muha.booking.service.RoomService;
import com.gmail.muha.booking.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private final UserService userService;
    private final RoomService roomService;
    private final HotelService hotelService;
    private final BookingRepository repository;
    private final BookingMapper mapper;

    public BookingServiceImpl(
            UserService userService, RoomService roomService, HotelService hotelService,
            BookingRepository repository, BookingMapper mapper) {
        this.userService = userService;
        this.roomService = roomService;
        this.hotelService = hotelService;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public BookingResponseDto createBooking(BookingCreateDto dto) {
        User user = userService.findUserEntityById(dto.getUserId());
        Room room = roomService.findRoomEntityById(dto.getRoomId());
        Hotel hotel = hotelService.findHotelEntityById(room.getHotelId());

        if (!isRoomAvailableForDates(dto)) {
            throw new AlreadyBookedException("Комната занята на это время");
        }

        Booking booking = mapper.toEntity(dto);

        booking = repository.save(booking);
        return mapper.toResponseDto(booking, user, room, hotel);
    }

    @Override
    public BookingResponseDto getBooking(Long id) {
        Booking booking =
                repository.findById(id).orElseThrow(() -> new NotFoundException("Booking not found with id: " + id));

        User user = userService.findUserEntityById(booking.getUserId());
        Room room = roomService.findRoomEntityById(booking.getRoomId());
        Hotel hotel = hotelService.findHotelEntityById(room.getHotelId());

        return mapper.toResponseDto(booking, user, room, hotel);
    }

    @Override
    public List<BookingResponseDto> getAllBookings() {
        return toDtoList(repository.findAll());
    }

    @Override
    public BookingResponseDto updateBooking(BookingUpdateDto bookingUpdateDto) {

        if (!isRoomAvailableForDates(bookingUpdateDto)) {
            throw new AlreadyBookedException("Комната занята на это время");
        }

        Booking booking = findBookingEntityById(bookingUpdateDto.getId());

        booking.setRoomId(bookingUpdateDto.getRoomId());
        booking.setStartDate(bookingUpdateDto.getStartDate());
        booking.setEndDate(bookingUpdateDto.getEndDate());

        User user = userService.findUserEntityById(booking.getUserId());
        Room room = roomService.findRoomEntityById(booking.getRoomId());
        Hotel hotel = hotelService.findHotelEntityById(room.getHotelId());

        booking = repository.update(booking);

        return mapper.toResponseDto(booking, user, room, hotel);
    }

    @Override
    public void deleteBooking(Long id) {
        getBooking(id);
        repository.deleteById(id);
    }

    @Override
    public List<BookingResponseDto> findByUserId(Long userId) {
        return toDtoList(repository.findByUserId(userId));
    }

    @Override
    public List<BookingResponseDto> findByRoomId(Long roomId) {
        return toDtoList(repository.findByRoomId(roomId));
    }

    @Override
    public Booking findBookingEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("booking not found with id: " + id));
    }

    private List<BookingResponseDto> toDtoList(List<Booking> bookings) {

        List<BookingResponseDto> bookingDtoList = new ArrayList<>();

        for (Booking booking : bookings) {

            User user = userService.findUserEntityById(booking.getUserId());
            Room room = roomService.findRoomEntityById(booking.getRoomId());
            Hotel hotel = hotelService.findHotelEntityById(room.getHotelId());
            bookingDtoList.add(mapper.toResponseDto(booking, user, room, hotel));
        }
        return bookingDtoList;
    }

    private boolean isRoomAvailableForDates(BookingCreateDto createBookingDto) {

        List<Booking> foundBookingsByRoomId = repository.findByRoomId(createBookingDto.getRoomId());

        for (Booking booking : foundBookingsByRoomId) {
            if (!notIntersectDates(createBookingDto, booking)) {
                return false;
            }
        }
        return true;
    }

    private boolean notIntersectDates(BookingCreateDto createDto, Booking booking) {
        return createDto.getEndDate().before(booking.getStartDate()) ||
                createDto.getStartDate().after(booking.getEndDate());
    }

    private boolean isRoomAvailableForDates(BookingUpdateDto updateDto) {

        List<Booking> foundBookingsByRoomId = repository.findByRoomId(updateDto.getRoomId());

        Booking bookingUpdate = findBookingEntityById(updateDto.getId());

        for (Booking booking : foundBookingsByRoomId) {
            if(booking.equals(bookingUpdate)){
                continue;
            }
            if (!notIntersectDates(updateDto, booking)) {
                return false;
            }
        }
        return true;
    }

    private boolean notIntersectDates(BookingUpdateDto updateDto, Booking booking) {
        return updateDto.getEndDate().before(booking.getStartDate()) ||
                updateDto.getStartDate().after(booking.getEndDate());
    }
}
