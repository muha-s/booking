package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.city_dto.CityCreateDto;
import com.gmail.muha.booking.dto.city_dto.CityResponseDto;
import com.gmail.muha.booking.entity.City;

import java.util.ArrayList;
import java.util.List;

public class CityMapper {
    private CityMapper() {
    }

    public City toEntity(CityCreateDto dto) {
        if (dto == null) {
            return null;
        }
        return new City(dto.getName());
    }

    public CityResponseDto toDto(City city) {
        if (city == null) {
            return null;
        }
        return new CityResponseDto(city.getId(), city.getName());
    }

    public List<CityResponseDto> toDtoList(List<City> cities) {

        List<CityResponseDto> cityDtoList = new ArrayList<>();

        for (City city : cities) {
            cityDtoList.add(toDto(city));
        }
        return cityDtoList;
    }

    private static class Holder {
        private static final CityMapper INSTANCE = new CityMapper();
    }

    public static CityMapper getInstance() {
        return Holder.INSTANCE;
    }
}
