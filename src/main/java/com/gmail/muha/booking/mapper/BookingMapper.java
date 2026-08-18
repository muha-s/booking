package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.booking.BookingCreateDto;
import com.gmail.muha.booking.dto.booking.BookingFullDto;
import com.gmail.muha.booking.dto.booking.BookingShortDto;
import com.gmail.muha.booking.dto.booking.BookingUpdateDto;
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
}
