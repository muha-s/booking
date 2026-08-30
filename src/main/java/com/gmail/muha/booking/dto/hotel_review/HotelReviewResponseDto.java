package com.gmail.muha.booking.dto.hotel_review;

import lombok.Data;

import java.time.Instant;

@Data
public class HotelReviewResponseDto {

    private Long id;
    private Integer score;
    private String comment;
    private Instant createdAt;
}
