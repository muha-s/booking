package com.gmail.muha.booking.dto.booking;

import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingManagedDto {

    private String userFirstName;
    private String userLastName;
    private String userPhone;
    private String userEmail;

    private RoomType roomType;
    private RoomCapacity roomCapacity;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private BookingStatus status;
}