package com.gmail.muha.booking.dto.booking;

import com.gmail.muha.booking.dto.hotel_review.HotelReviewDto;
import com.gmail.muha.booking.dto.room.RoomFullDto;
import com.gmail.muha.booking.dto.user.UserShortDto;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingFullDto {

    private Long id;
    private UserShortDto user;
    private RoomFullDto room;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus status;
    private BigDecimal totalPrice;
    private HotelReviewDto review;

}
