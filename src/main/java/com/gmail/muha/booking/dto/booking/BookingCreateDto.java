package com.gmail.muha.booking.dto.booking;

import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingCreateDto {

    @NotNull(message = "User id cannot be null")
    private Long userId;

    @NotNull(message = "Hotel id cannot be null")
    private Long hotelId;

    @NotNull(message = "Room capacity cannot be null")
    private RoomCapacity roomCapacity;

    @NotNull(message = "Room type cannot be null")
    private RoomType roomType;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date can't be in the past")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date cannot be past or present")
    private LocalDate endDate;

}
