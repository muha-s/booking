package com.gmail.muha.booking.dto.user;

import com.gmail.muha.booking.model.entity.enums.UserRole;
import lombok.Data;

@Data
public class UserShortDto {

    private Long id;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String phone;

}
