package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.booking_dto.BookingCreateDto;
import com.gmail.muha.booking.dto.booking_dto.BookingResponseDto;
import com.gmail.muha.booking.dto.booking_dto.BookingUpdateDto;
import com.gmail.muha.booking.dto.user_dto.UserUpdateDto;
import com.gmail.muha.booking.entity.Booking;
import com.gmail.muha.booking.entity.Hotel;
import com.gmail.muha.booking.entity.Room;
import com.gmail.muha.booking.entity.User;

public class BookingMapper {


    public BookingMapper() {
    }

    public Booking toEntity(BookingCreateDto dto) {
        if (dto == null) {
            return null;
        }
        return new Booking(dto.getUserId(), dto.getRoomId(), dto.getStartDate(), dto.getEndDate());
    }

    public BookingResponseDto toResponseDto(Booking booking, User user, Room room, Hotel hotel) {
        if (booking == null) {
            return null;
        }
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setUserName(user.getName());
        dto.setRoomType(room.getRoomType());
        dto.setHotelName(hotel.getName());
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());
        return dto;
    }

    public BookingUpdateDto toUpdateDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return new BookingUpdateDto(
                booking.getId(), booking.getRoomId(), booking.getStartDate(), booking.getEndDate());
    }

    private static class Holder {
        private static final BookingMapper INSTANCE = new BookingMapper();
    }

    public static BookingMapper getInstance() {
        return BookingMapper.Holder.INSTANCE;
    }
}
