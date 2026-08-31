package com.gmail.muha.booking.dto.city;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CityCreateDto {

    @NotBlank(message = "City name cannot be blank")
    @Size(min = 2, max = 30, message = "City must be between 2 and 30 characters")
    private String name;
}
