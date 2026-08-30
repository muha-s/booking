package com.gmail.muha.booking.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {

    @Size(min = 1, max = 20, message = "First name must be between 1 and 20")
    private String firstName;

    @Size(min = 1, max = 20, message = "Last name must be between 1 and 20")
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9()\\-\\s]{7,20}$", message = "Phone format is invalid")
    private String phone;

}
