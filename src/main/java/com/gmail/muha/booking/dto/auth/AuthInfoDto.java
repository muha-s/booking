package com.gmail.muha.booking.dto.auth;

import com.gmail.muha.booking.model.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthInfoDto {

    private UserRole role;
}