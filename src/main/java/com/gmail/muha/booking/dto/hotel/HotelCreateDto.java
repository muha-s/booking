package com.gmail.muha.booking.dto.hotel;

import com.gmail.muha.booking.model.entity.enums.NumberOfStars;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HotelCreateDto {

    @NotBlank(message = "Hotel name cannot be blank")
    @Size(min = 2, max = 30, message = "Hotel name must be between 2 and 30 characters")
    private String name;

    @NotNull(message = "City id cannot be null")
    private Long cityId;

    @NotBlank(message = "Address cannot be blank")
    @Size(min = 5, max = 50, message = "Address must be between 5 and 50 characters")
    private String address;

    @NotNull(message = "Number of stars cannot be null")
    private NumberOfStars numberOfStars;

    @NotNull(message = "Base price per night cannot be null")
    @Positive(message = "Base price per night must be greater than zero")
    private BigDecimal basePricePerNight;

}
