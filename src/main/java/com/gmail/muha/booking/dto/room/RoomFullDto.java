package com.gmail.muha.booking.dto.room;

import com.gmail.muha.booking.dto.booking.BookingShortDto;
import com.gmail.muha.booking.dto.hotel.HotelShortDto;
import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomFullDto {

    private Long id;
    private HotelShortDto hotel;
    private RoomCapacity roomCapacity;
    private RoomType roomType;
    private List<BookingShortDto> bookings = new ArrayList<>();

}
