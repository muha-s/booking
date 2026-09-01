package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.city.CityCreateDto;
import com.gmail.muha.booking.dto.city.CityShortDto;
import com.gmail.muha.booking.model.entity.City;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CityMapperTest {

    private final CityMapper cityMapper = new CityMapper();

    @Test
    void shouldMapCityToShortDto() {
        // given
        City city = new City();
        city.setId(1L);
        city.setName("Madrid");

        // when
        CityShortDto result = cityMapper.toShortDto(city);

        // then
        assertEquals(1L, result.getId());
        assertEquals("Madrid", result.getName());
    }

    @Test
    void shouldMapCityCreateDtoToEntity() {
        // given
        CityCreateDto cityCreateDto = new CityCreateDto();
        cityCreateDto.setName("Barcelona");

        // when
        City result = cityMapper.toEntity(cityCreateDto);

        // then
        assertEquals("Barcelona", result.getName());
    }

    @Test
    void shouldMapCitiesToShortDtoList() {
        // given
        City firstCity = new City();
        firstCity.setId(1L);
        firstCity.setName("Madrid");

        City secondCity = new City();
        secondCity.setId(2L);
        secondCity.setName("Barcelona");

        List<City> cities = List.of(firstCity, secondCity);

        // when
        List<CityShortDto> result = cityMapper.toShortDtoList(cities);

        // then
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("Madrid", result.get(0).getName());

        assertEquals(2L, result.get(1).getId());
        assertEquals("Barcelona", result.get(1).getName());
    }
}