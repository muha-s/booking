package com.gmail.muha.booking.dto.hotel_review;

import com.gmail.muha.booking.dto.booking.BookingShortDto;
import lombok.Data;

import java.time.Instant;

@Data
public class HotelReviewDto {

    private Long id;
    private BookingShortDto booking;
    private Double rating;
    private String comment;
    private Instant createdAt;
}
