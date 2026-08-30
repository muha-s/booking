package com.gmail.muha.booking.dto.room;

import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RoomSearchCriteriaDto {

    @NotNull(message = "City id cannot be null")
    private Long cityId;

    private RoomType roomType;

    private RoomCapacity roomCapacity;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date can't be in the past")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date cannot be past or present")
    private LocalDate endDate;


}
