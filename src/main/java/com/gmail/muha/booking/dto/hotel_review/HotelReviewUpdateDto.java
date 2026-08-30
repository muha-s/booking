package com.gmail.muha.booking.dto.hotel_review;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HotelReviewUpdateDto {

    @DecimalMin(value = "0.0", message = "Score cannot be less than 0")
    @DecimalMax(value = "10.0", message = "Score cannot be greater than 10")
    private Integer score;

    @Size(min = 1, max = 200, message = "Comment must be between 1 and 200 characters")
    private String comment;
}
