package com.gmail.muha.booking.dto.hotel;

import com.gmail.muha.booking.dto.city.CityShortDto;
import com.gmail.muha.booking.model.entity.enums.NumberOfStars;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class HotelShortDto {

    private Long id;
    private String name;
    private CityShortDto city;
    private String address;
    private NumberOfStars numberOfStars;
    private Double rating;
    private BigDecimal basePricePerNight;

}
