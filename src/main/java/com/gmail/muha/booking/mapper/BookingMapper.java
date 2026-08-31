package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.booking.*;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.Room;
import com.gmail.muha.booking.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingMapper {

    private final ShortDtoMapper shortDtoMapper;


    public BookingMapper(ShortDtoMapper shortDtoMapper) {
        this.shortDtoMapper = shortDtoMapper;
    }

    public BookingManagedDto toManagedDto(Booking booking) {
        BookingManagedDto bookingManagedDto = new BookingManagedDto();

        bookingManagedDto.setUserFirstName(booking.getUser().getFirstName());
        bookingManagedDto.setUserLastName(booking.getUser().getLastName());
        bookingManagedDto.setUserPhone(booking.getUser().getPhone());
        bookingManagedDto.setUserEmail(booking.getUser().getEmail());

        bookingManagedDto.setRoomType(booking.getRoom().getRoomType());
        bookingManagedDto.setRoomCapacity(booking.getRoom().getRoomCapacity());

        bookingManagedDto.setStartDate(booking.getStartDate());
        bookingManagedDto.setEndDate(booking.getEndDate());
        bookingManagedDto.setTotalPrice(booking.getTotalPrice());
        bookingManagedDto.setStatus(booking.getStatus());

        return bookingManagedDto;
    }

    public BookingForUserDto toForUserDto(Booking booking) {

        BookingForUserDto bookingForUserDto = new BookingForUserDto();

        bookingForUserDto.setId(booking.getId());
        bookingForUserDto.setRoom(shortDtoMapper.toRoomShortDto(booking.getRoom()));
        bookingForUserDto.setStartDate(booking.getStartDate());
        bookingForUserDto.setEndDate(booking.getEndDate());
        bookingForUserDto.setStatus(booking.getStatus());
        bookingForUserDto.setTotalPrice(booking.getTotalPrice());

        return bookingForUserDto;
    }

    public BookingForReviewDto toForReviewDto(Booking booking) {

        BookingForReviewDto bookingForReviewDto = new BookingForReviewDto();

        bookingForReviewDto.setId(booking.getId());

        bookingForReviewDto.setHotelName(booking.getRoom().getHotel().getName());
        bookingForReviewDto.setCityName(booking.getRoom().getHotel().getCity().getName());
        bookingForReviewDto.setHotelAddress(booking.getRoom().getHotel().getAddress());
        bookingForReviewDto.setNumberOfStars(booking.getRoom().getHotel().getNumberOfStars());

        bookingForReviewDto.setRoomCapacity(booking.getRoom().getRoomCapacity());
        bookingForReviewDto.setRoomType(booking.getRoom().getRoomType());

        bookingForReviewDto.setStartDate(booking.getStartDate());
        bookingForReviewDto.setEndDate(booking.getEndDate());
        bookingForReviewDto.setStatus(booking.getStatus());

        bookingForReviewDto.setReviewExists(booking.getReview() != null);

        return bookingForReviewDto;
    }

    public Booking toEntity(BookingCreateDto bookingCreateDto, User bookingUser, Room selectedRoom) {
        Booking booking = new Booking();

        booking.setUser(bookingUser);
        booking.setRoom(selectedRoom);
        booking.setStartDate(bookingCreateDto.getStartDate());
        booking.setEndDate(bookingCreateDto.getEndDate());

        return booking;
    }

    public void updateEntity(BookingUpdateDto bookingUpdateDto, Booking entity, Room selectedRoom) {
        entity.setRoom(selectedRoom);
        entity.setStartDate(bookingUpdateDto.getStartDate());
        entity.setEndDate(bookingUpdateDto.getEndDate());
    }

    public List<BookingForUserDto> toForUserDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toForUserDto)
                .toList();
    }

    public List<BookingManagedDto> toManagedDtoList(List<Booking> bookings) {
        return bookings.stream().map(this::toManagedDto).toList();
    }
}
