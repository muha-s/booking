package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.hotel_review.HotelReviewCreateDto;
import com.gmail.muha.booking.dto.hotel_review.HotelReviewForHotelDto;
import com.gmail.muha.booking.dto.hotel_review.HotelReviewResponseDto;
import com.gmail.muha.booking.service.hotel.HotelReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel-reviews")
@RequiredArgsConstructor
public class HotelReviewController {

    private final HotelReviewService hotelReviewService;

    @PostMapping
    public HotelReviewResponseDto create(
            @Valid @RequestBody HotelReviewCreateDto hotelReviewCreateDto, Authentication authentication) {

        return hotelReviewService.create(hotelReviewCreateDto, authentication.getName());
    }

    @GetMapping("/hotel/{hotelId}")
    public List<HotelReviewForHotelDto> findCommentsByHotelId(@PathVariable Long hotelId) {
        return hotelReviewService.findCommentsByHotelId(hotelId);
    }
}