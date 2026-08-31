package com.gmail.muha.booking.service.hotel;

import com.gmail.muha.booking.dto.hotel_review.HotelReviewCreateDto;
import com.gmail.muha.booking.dto.hotel_review.HotelReviewForHotelDto;
import com.gmail.muha.booking.dto.hotel_review.HotelReviewResponseDto;

import java.util.List;

public interface HotelReviewService {


    HotelReviewResponseDto create(HotelReviewCreateDto hotelReviewCreateDto, String userEmail);

    void sendReviewRequests();

    Long countCommentsByHotelId(Long hotelId);

    List<HotelReviewForHotelDto> findCommentsByHotelId(Long hotelId);

}
