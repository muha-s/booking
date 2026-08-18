package com.gmail.muha.booking.dto.room;

import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RoomBatchCreateDto {

    @NotNull(message = "Hotel id cannot be null")
    private Long hotelId;

    @NotNull(message = "Room capacity cannot be null")
    private RoomCapacity roomCapacity;

    @NotNull(message = "Room type cannot be null")
    private RoomType roomType;


    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;
}
