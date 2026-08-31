package com.gmail.muha.booking.dto.hotel;

import com.gmail.muha.booking.dto.city.CityShortDto;
import lombok.Data;

@Data
public class HotelManagedDto {

    private Long id;
    private String name;
    private CityShortDto city;
    private String address;
}
