package com.gmail.muha.booking.dto.hotel_review;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class HotelReviewCreateDto {


    @NotNull(message = "Booking id cannot be null")
    private Long bookingId;

    @Min(value = 0, message = "Score cannot be less than 0")
    @Max(value = 10, message = "Score cannot be greater than 10")
    private Integer score;

    @Size(min = 1, max = 200, message = "Comment must be between 1 and 200 characters")
    private String comment;

}
