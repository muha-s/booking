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
    private final FullDtoMapper fullDtoMapper;

    public BookingMapper(ShortDtoMapper shortDtoMapper, FullDtoMapper fullDtoMapper) {
        this.shortDtoMapper = shortDtoMapper;
        this.fullDtoMapper = fullDtoMapper;
    }

    public BookingFullDto toFullDto(Booking booking) {

        BookingFullDto bookingFullDto = new BookingFullDto();

        bookingFullDto.setId(booking.getId());
        bookingFullDto.setUser(shortDtoMapper.toUserShortDto(booking.getUser()));
        bookingFullDto.setRoom(fullDtoMapper.toRoomFullDto(booking.getRoom()));
        bookingFullDto.setStartDate(booking.getStartDate());
        bookingFullDto.setEndDate(booking.getEndDate());
        bookingFullDto.setStatus(booking.getStatus());
        bookingFullDto.setTotalPrice(booking.getTotalPrice());

        if(booking.getReview() != null){
            bookingFullDto.setReview(fullDtoMapper.toHotelReviewDto(booking.getReview()));
        }
        return bookingFullDto;
    }

    public BookingShortDto toShortDto(Booking booking) {

        BookingShortDto bookingShortDto = new BookingShortDto();

        bookingShortDto.setId(booking.getId());
        bookingShortDto.setUser(shortDtoMapper.toUserShortDto(booking.getUser()));
        bookingShortDto.setRoom(shortDtoMapper.toRoomShortDto(booking.getRoom()));
        bookingShortDto.setStartDate(booking.getStartDate());
        bookingShortDto.setEndDate(booking.getEndDate());
        bookingShortDto.setStatus(booking.getStatus());
        bookingShortDto.setTotalPrice(booking.getTotalPrice());

        return bookingShortDto;
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

    public List<BookingShortDto> toShortDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toShortDto)
                .toList();
    }

    public List<BookingForUserDto> toForUserDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toForUserDto)
                .toList();
    }
}
