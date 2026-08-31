package com.gmail.muha.booking.dto.user;

import com.gmail.muha.booking.dto.hotel.HotelShortDto;
import lombok.Data;

import java.util.Set;

@Data
public class HotelAdminDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private boolean emailVerified;
    private Set<HotelShortDto> managedHotels;
}