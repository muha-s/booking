package com.gmail.muha.booking.dto.booking;

import com.gmail.muha.booking.dto.room.RoomShortDto;
import com.gmail.muha.booking.dto.user.UserShortDto;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingShortDto {

    private Long id;
    private UserShortDto user;
    private RoomShortDto room;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus status;
    private BigDecimal totalPrice;
}
