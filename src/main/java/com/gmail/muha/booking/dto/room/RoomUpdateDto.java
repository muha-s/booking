package com.gmail.muha.booking.dto.room;

import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import lombok.Data;

@Data
public class RoomUpdateDto {


    private RoomCapacity roomCapacity;
    private RoomType roomType;
}
