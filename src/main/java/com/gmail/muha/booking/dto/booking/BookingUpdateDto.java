package com.gmail.muha.booking.dto.booking;

import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingUpdateDto {


    private RoomCapacity roomCapacity;
    private RoomType roomType;

    @FutureOrPresent(message = "Start date can't be in the past")
    private LocalDate startDate;

    @Future(message = "End date cannot be past or present")
    private LocalDate endDate;
}
