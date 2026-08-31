package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.hotel_review.HotelReviewCreateDto;
import com.gmail.muha.booking.dto.hotel_review.HotelReviewForHotelDto;
import com.gmail.muha.booking.dto.hotel_review.HotelReviewResponseDto;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.HotelReview;
import org.springframework.stereotype.Component;

@Component
public class HotelReviewMapper {


    public HotelReview toEntity(HotelReviewCreateDto hotelReviewCreateDto, Booking hotelReviewBooking) {
        HotelReview hotelReview = new HotelReview();

        hotelReview.setBooking(hotelReviewBooking);
        hotelReview.setScore(hotelReviewCreateDto.getScore());
        hotelReview.setComment(hotelReviewCreateDto.getComment());

        return hotelReview;
    }

    public HotelReviewResponseDto toHotelReviewResponseDto(HotelReview hotelReview) {
        HotelReviewResponseDto hotelReviewResponseDto = new HotelReviewResponseDto();

        hotelReviewResponseDto.setId(hotelReview.getId());
        hotelReviewResponseDto.setScore(hotelReview.getScore());
        hotelReviewResponseDto.setComment(hotelReview.getComment());
        hotelReviewResponseDto.setCreatedAt(hotelReview.getCreatedAt());

        return hotelReviewResponseDto;
    }

    public HotelReviewForHotelDto toHotelReviewForHotelDto(HotelReview hotelReview) {
        HotelReviewForHotelDto dto = new HotelReviewForHotelDto();

        dto.setId(hotelReview.getId());
        dto.setAuthorName(hotelReview.getBooking().getUser().getFirstName());
        dto.setScore(hotelReview.getScore());
        dto.setComment(hotelReview.getComment());
        dto.setCreatedAt(hotelReview.getCreatedAt());

        return dto;
    }
}
