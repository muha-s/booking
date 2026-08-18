package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.city.CityCreateDto;
import com.gmail.muha.booking.dto.city.CityFullDto;
import com.gmail.muha.booking.dto.city.CityShortDto;
import com.gmail.muha.booking.dto.city.CityUpdateDto;
import com.gmail.muha.booking.model.entity.City;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityMapper {

    private final ShortDtoMapper shortDtoMapper;

    public CityMapper(ShortDtoMapper shortDtoMapper) {
        this.shortDtoMapper = shortDtoMapper;
    }

    public CityFullDto toFullDto(City city) {

        CityFullDto cityFullDto = new CityFullDto();

        cityFullDto.setId(city.getId());
        cityFullDto.setName(city.getName());
        cityFullDto.setHotels(shortDtoMapper.toHotelShortDtoList(city.getHotels()));

        return cityFullDto;
    }

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

    public void updateEntity(CityUpdateDto cityUpdateDto, City entity) {

        if (cityUpdateDto.getName() != null) {
            entity.setName(cityUpdateDto.getName());
        }
    }

    public List<CityShortDto> toShortDtoList(List<City> cities) {
        return cities.stream()
                .map(this::toShortDto)
                .toList();
    }
}
