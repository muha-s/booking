package com.gmail.muha.booking.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserUpdateDto {

    @Size(min = 1, max = 20, message = "First name must be between 1 and 20")
    private String firstName;

    @Size(min = 1, max = 20, message = "Last name must be between 1 and 20")
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9()\\-\\s]{7,20}$", message = "Phone format is invalid")
    private String phone;

    @Email(message = "Email format is invalid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

//    @Size(min = 8, max = 100, message = "Password length must be between 8 and 100")
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Password must contain at least one letter and one digit")
//    private String password;

    @PositiveOrZero(message = "Balance cannot be negative")
    private BigDecimal balance;
}
