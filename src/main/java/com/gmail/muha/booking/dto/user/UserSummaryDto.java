package com.gmail.muha.booking.dto.user;

import com.gmail.muha.booking.model.entity.enums.UserRole;
import lombok.Data;

import java.time.Instant;

@Data
public class UserSummaryDto {

    private Long id;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Instant deletedAt;

}
