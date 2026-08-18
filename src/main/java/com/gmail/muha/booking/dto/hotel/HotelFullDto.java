package com.gmail.muha.booking.dto.hotel;

import com.gmail.muha.booking.dto.city.CityShortDto;
import com.gmail.muha.booking.dto.room.RoomShortDto;
import com.gmail.muha.booking.model.entity.enums.NumberOfStars;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HotelFullDto {

    private Long id;
    private String name;
    private CityShortDto city;
    private String address;
    private NumberOfStars numberOfStars;
    private Double rating;
    private BigDecimal basePricePerNight;
    private BigDecimal balance;
    private List<RoomShortDto> rooms = new ArrayList<>();

}
