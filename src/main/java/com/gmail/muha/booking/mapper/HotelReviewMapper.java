package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.hotel_review.HotelReviewCreateDto;
import com.gmail.muha.booking.dto.hotel_review.HotelReviewDto;
import com.gmail.muha.booking.dto.hotel_review.HotelReviewUpdateDto;
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

    public HotelReviewDto toDto(HotelReview hotelReview) {

        HotelReviewDto hotelReviewDto = new HotelReviewDto();
        hotelReviewDto.setId(hotelReview.getId());
        hotelReviewDto.setBooking(shortDtoMapper.toBookingShortDto(hotelReview.getBooking()));
        hotelReviewDto.setRating(hotelReview.getRating());
        hotelReviewDto.setComment(hotelReview.getComment());
        hotelReviewDto.setCreatedAt(hotelReview.getCreatedAt());

        return hotelReviewDto;
    }

    public HotelReview toEntity(HotelReviewCreateDto hotelReviewCreateDto, Booking hotelReviewBooking) {
        HotelReview hotelReview = new HotelReview();

        hotelReview.setBooking(hotelReviewBooking);
        hotelReview.setRating(hotelReviewCreateDto.getRating());
        hotelReview.setComment(hotelReviewCreateDto.getComment());

        return hotelReview;
    }

    public void updateEntity(HotelReviewUpdateDto hotelReviewUpdateDto, HotelReview entity) {

        if (hotelReviewUpdateDto.getRating() != null) {
            entity.setRating(hotelReviewUpdateDto.getRating());
        }

        if (hotelReviewUpdateDto.getComment() != null) {
            entity.setComment(hotelReviewUpdateDto.getComment());
        }
    }

    public List<HotelReviewDto> toDtoList(List<HotelReview> hotelReviews) {
        return hotelReviews.stream()
                .map(this::toDto)
                .toList();
    }
}
