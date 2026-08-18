package com.gmail.muha.booking.dto.hotel_review;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class HotelReviewCreateDto {


    @NotNull(message = "Booking id cannot be null")
    private Long bookingId;

    @NotNull(message = "Rating cannot be null")
    @DecimalMin(value = "0.0", message = "Rating cannot be less than 0")
    @DecimalMax(value = "10.0", message = "Rating cannot be greater than 10")
    private Double rating;

    @Size(min = 1, max = 200, message = "Comment must be between 1 and 200 characters")
    private String comment;

}
