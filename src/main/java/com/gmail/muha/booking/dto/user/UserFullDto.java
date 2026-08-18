package com.gmail.muha.booking.dto.user;

import com.gmail.muha.booking.dto.booking.BookingShortDto;
import com.gmail.muha.booking.dto.hotel.HotelShortDto;
import com.gmail.muha.booking.model.entity.enums.UserRole;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserFullDto {

    private Long id;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private BigDecimal balance;
    private List<BookingShortDto> bookings = new ArrayList<>();
    private Set<HotelShortDto> managedHotels = new HashSet<>();
}
