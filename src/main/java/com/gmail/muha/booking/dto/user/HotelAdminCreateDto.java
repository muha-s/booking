package com.gmail.muha.booking.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HotelAdminCreateDto {

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 1, max = 20, message = "First name must be between 1 and 20")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 1, max = 20, message = "Last name must be between 1 and 20")
    private String lastName;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "^\\+?[0-9()\\-\\s]{7,20}$", message = "Phone format is invalid")
    private String phone;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email format is invalid")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email format is invalid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

}