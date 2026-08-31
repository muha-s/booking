package com.gmail.muha.booking.dto.room;

import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomManagedCreateDto {

    @NotNull(message = "Room capacity cannot be null")
    private RoomCapacity roomCapacity;

    @NotNull(message = "Room type cannot be null")
    private RoomType roomType;
}