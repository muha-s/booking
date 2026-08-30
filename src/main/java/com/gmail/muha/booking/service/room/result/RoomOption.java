package com.gmail.muha.booking.service.room.result;

import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;

public record RoomOption(Long hotelId, RoomType roomType, RoomCapacity roomCapacity) {
}
