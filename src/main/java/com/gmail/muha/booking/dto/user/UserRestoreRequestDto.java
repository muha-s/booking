package com.gmail.muha.booking.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRestoreRequestDto {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    private String email;
}