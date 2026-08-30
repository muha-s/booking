package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.hotel_review.*;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.HotelReview;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HotelReviewMapper {

    private final ShortDtoMapper shortDtoMapper;

    public HotelReviewMapper(ShortDtoMapper shortDtoMapper) {
        this.shortDtoMapper = shortDtoMapper;
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

    public void updateEntity(HotelReviewUpdateDto hotelReviewUpdateDto, HotelReview entity) {

        if (hotelReviewUpdateDto.getScore() != null) {
            entity.setScore(hotelReviewUpdateDto.getScore());
        }

        if (hotelReviewUpdateDto.getComment() != null) {
            entity.setComment(hotelReviewUpdateDto.getComment());
        }
    }

    public List<HotelReviewDto> toHotelReviewDtoList(List<HotelReview> hotelReviews) {
        return hotelReviews.stream()
                .map(this::toHotelReviewDto)
                .toList();
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
