package com.gmail.muha.booking.service.hotel;

import com.gmail.muha.booking.dto.hotel_review.*;
import com.gmail.muha.booking.model.entity.HotelReview;

import java.util.List;

public interface HotelReviewService {

    HotelReviewDto findById(Long id);

    HotelReview findEntityById(Long id);

    List<HotelReviewDto> findAll();

    HotelReviewResponseDto create(HotelReviewCreateDto hotelReviewCreateDto, String userEmail);

    HotelReviewDto update(Long id, HotelReviewUpdateDto hotelReviewUpdateDto);

    void deleteById(Long id);

    void sendReviewRequests();

    Long countCommentsByHotelId(Long hotelId);

    List<HotelReviewForHotelDto> findCommentsByHotelId(Long hotelId);
}
