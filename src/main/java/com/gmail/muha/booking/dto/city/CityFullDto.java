package com.gmail.muha.booking.dto.city;

import com.gmail.muha.booking.dto.hotel.HotelShortDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CityFullDto {

    private Long id;
    private String name;
    private List<HotelShortDto> hotels = new ArrayList<>();


}
