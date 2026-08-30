package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.hotel_review.HotelReviewDto;
import com.gmail.muha.booking.dto.room.RoomFullDto;
import com.gmail.muha.booking.model.entity.HotelReview;
import com.gmail.muha.booking.model.entity.Room;
import org.springframework.stereotype.Component;

@Component
class FullDtoMapper {

    private final ShortDtoMapper shortDtoMapper;

    public FullDtoMapper(ShortDtoMapper shortDtoMapper) {
        this.shortDtoMapper = shortDtoMapper;
    }

    public RoomFullDto toRoomFullDto(Room room) {

        RoomFullDto roomFullDto = new RoomFullDto();

        roomFullDto.setId(room.getId());
        roomFullDto.setHotel(shortDtoMapper.toHotelShortDto(room.getHotel()));
        roomFullDto.setRoomCapacity(room.getRoomCapacity());
        roomFullDto.setRoomType(room.getRoomType());
        roomFullDto.setBookings(shortDtoMapper.toBookingShortDtoList(room.getBookings()));

        return roomFullDto;
    }

    public HotelReviewDto toHotelReviewDto(HotelReview hotelReview) {

        HotelReviewDto hotelReviewDto = new HotelReviewDto();
        hotelReviewDto.setId(hotelReview.getId());
        hotelReviewDto.setBooking(shortDtoMapper.toBookingShortDto(hotelReview.getBooking()));
        hotelReviewDto.setScore(hotelReview.getScore());
        hotelReviewDto.setComment(hotelReview.getComment());
        hotelReviewDto.setCreatedAt(hotelReview.getCreatedAt());

        return hotelReviewDto;
    }
}
