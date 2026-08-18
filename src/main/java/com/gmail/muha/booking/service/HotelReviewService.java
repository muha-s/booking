package com.gmail.muha.booking.service;

import com.gmail.muha.booking.dto.hotel_review.HotelReviewCreateDto;
import com.gmail.muha.booking.dto.hotel_review.HotelReviewDto;
import com.gmail.muha.booking.dto.hotel_review.HotelReviewUpdateDto;
import com.gmail.muha.booking.model.entity.HotelReview;

import java.util.List;

public interface HotelReviewService {

    HotelReviewDto findById(Long id);

    HotelReview findEntityById(Long id);

    List<HotelReviewDto> findAll();

    HotelReviewDto create(HotelReviewCreateDto hotelReviewCreateDto);

    HotelReviewDto update(Long id, HotelReviewUpdateDto hotelReviewUpdateDto);

    void deleteById(Long id);
}
