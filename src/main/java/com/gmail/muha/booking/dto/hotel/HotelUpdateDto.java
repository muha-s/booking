package com.gmail.muha.booking.dto.hotel;

import com.gmail.muha.booking.model.entity.enums.NumberOfStars;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HotelUpdateDto {

    @Size(min = 2, max = 30, message = "Hotel name must be between 2 and 30 characters")
    private String name;

    @Size(min = 5, max = 50, message = "Address must be between 5 and 50 characters")
    private String address;

    private NumberOfStars numberOfStars;

    @Positive(message = "Base price per night must be greater than zero")
    private BigDecimal basePricePerNight;
}
