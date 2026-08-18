package com.gmail.muha.booking.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDto {

    @NotBlank(message = "Old password can not be blank")
    private String oldPassword;

    @NotBlank(message = "New password can not be blank")
    @Size(min = 8, max = 100, message = "Password length must be between 8 and 100")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Password must contain at least one letter and one digit")
    private String newPassword;
}
