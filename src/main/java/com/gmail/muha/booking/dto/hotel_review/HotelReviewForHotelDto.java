package com.gmail.muha.booking.dto.hotel_review;

import lombok.Data;

import java.time.Instant;

@Data
public class HotelReviewForHotelDto {

    private Long id;
    private String authorName;
    private Integer score;
    private String comment;
    private Instant createdAt;
}
