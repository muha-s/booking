package com.gmail.muha.booking.dto.room;

import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomCreateDto {

    @NotNull(message = "Hotel id cannot be null")
    private Long hotelId;

    @NotNull(message = "Room сapacity cannot be null")
    private RoomCapacity roomCapacity;

    @NotNull(message = "Room type cannot be null")
    private RoomType roomType;

}
