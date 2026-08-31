package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.city.CityCreateDto;
import com.gmail.muha.booking.dto.city.CityShortDto;
import com.gmail.muha.booking.model.entity.City;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityMapper {


    public CityShortDto toShortDto(City city) {
        CityShortDto cityShortDto = new CityShortDto();

        cityShortDto.setId(city.getId());
        cityShortDto.setName(city.getName());

        return cityShortDto;
    }

    public City toEntity(CityCreateDto cityCreateDto) {
        City city = new City();

        city.setName(cityCreateDto.getName());

        return city;
    }

    public List<CityShortDto> toShortDtoList(List<City> cities) {
        return cities.stream()
                .map(this::toShortDto)
                .toList();
    }
}
