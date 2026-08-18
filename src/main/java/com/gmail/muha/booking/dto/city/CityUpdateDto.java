package com.gmail.muha.booking.dto.city;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class CityUpdateDto {

    @NotBlank(message = "City name cannot be blank")
    @Size(min = 2, max = 30, message = "City must be between 2 and 30 characters")
    private String name;
}
