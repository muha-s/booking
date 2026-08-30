package com.gmail.muha.booking.dto.booking;

import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import com.gmail.muha.booking.model.entity.enums.NumberOfStars;
import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingForReviewDto {

    private Long id;
    private String hotelName;
    private String cityName;
    private String hotelAddress;
    private NumberOfStars numberOfStars;
    private RoomCapacity roomCapacity;
    private RoomType roomType;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus status;
    private boolean reviewExists;
}
