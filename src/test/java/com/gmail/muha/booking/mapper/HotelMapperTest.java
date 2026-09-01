package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.hotel.HotelCreateDto;
import com.gmail.muha.booking.dto.hotel.HotelManagedDto;
import com.gmail.muha.booking.dto.hotel.HotelManagedFullDto;
import com.gmail.muha.booking.dto.hotel.HotelShortDto;
import com.gmail.muha.booking.dto.hotel.HotelUpdateDto;
import com.gmail.muha.booking.model.entity.City;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.enums.NumberOfStars;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class HotelMapperTest {

    private final ShortDtoMapper shortDtoMapper = new ShortDtoMapper();
    private final HotelMapper hotelMapper = new HotelMapper(shortDtoMapper);

    @Test
    void shouldMapHotelToManagedFullDto() {
        // given
        City city = new City();
        city.setId(1L);
        city.setName("Madrid");

        NumberOfStars stars = NumberOfStars.values()[0];

        Hotel hotel = new Hotel();
        hotel.setId(10L);
        hotel.setName("Central Hotel");
        hotel.setCity(city);
        hotel.setAddress("Main Street 10");
        hotel.setNumberOfStars(stars);
        hotel.setBasePricePerNight(new BigDecimal("120.00"));
        hotel.setBalance(new BigDecimal("5000.00"));

        // when
        HotelManagedFullDto result = hotelMapper.toManagedFullDto(hotel);

        // then
        assertEquals(10L, result.getId());
        assertEquals("Central Hotel", result.getName());
        assertEquals(1L, result.getCity().getId());
        assertEquals("Madrid", result.getCity().getName());
        assertEquals("Main Street 10", result.getAddress());
        assertEquals(stars, result.getNumberOfStars());
        assertEquals(new BigDecimal("120.00"), result.getBasePricePerNight());
        assertEquals(new BigDecimal("5000.00"), result.getBalance());
    }

    @Test
    void shouldMapHotelToShortDto() {
        // given
        City city = new City();
        city.setId(1L);
        city.setName("Madrid");

        Hotel hotel = new Hotel();
        hotel.setId(10L);
        hotel.setName("Central Hotel");
        hotel.setCity(city);
        hotel.setAddress("Main Street 10");

        // when
        HotelShortDto result = hotelMapper.toShortDto(hotel);

        // then
        assertEquals(10L, result.getId());
        assertEquals("Central Hotel", result.getName());
        assertEquals(1L, result.getCity().getId());
        assertEquals("Madrid", result.getCity().getName());
        assertEquals("Main Street 10", result.getAddress());
    }

    @Test
    void shouldMapHotelToManagedDto() {
        // given
        City city = new City();
        city.setId(1L);
        city.setName("Madrid");

        Hotel hotel = new Hotel();
        hotel.setId(10L);
        hotel.setName("Central Hotel");
        hotel.setCity(city);
        hotel.setAddress("Main Street 10");

        // when
        HotelManagedDto result = hotelMapper.toManagedDto(hotel);

        // then
        assertEquals(10L, result.getId());
        assertEquals("Central Hotel", result.getName());
        assertEquals(1L, result.getCity().getId());
        assertEquals("Madrid", result.getCity().getName());
        assertEquals("Main Street 10", result.getAddress());
    }

    @Test
    void shouldMapHotelCreateDtoToEntity() {
        // given
        City city = new City();
        city.setId(1L);
        city.setName("Madrid");

        NumberOfStars stars = NumberOfStars.FIVE_STARS;

        HotelCreateDto hotelCreateDto = new HotelCreateDto();
        hotelCreateDto.setName("Central Hotel");
        hotelCreateDto.setAddress("Main Street 10");
        hotelCreateDto.setNumberOfStars(stars);
        hotelCreateDto.setBasePricePerNight(new BigDecimal("120.00"));

        // when
        Hotel result = hotelMapper.toEntity(hotelCreateDto, city);

        // then
        assertEquals("Central Hotel", result.getName());
        assertSame(city, result.getCity());
        assertEquals("Main Street 10", result.getAddress());
        assertEquals(stars, result.getNumberOfStars());
        assertEquals(new BigDecimal("120.00"), result.getBasePricePerNight());
    }

    @Test
    void shouldUpdateHotelEntity() {
        // given
        NumberOfStars oldStars = NumberOfStars.values()[0];
        NumberOfStars newStars = NumberOfStars.values()[
                Math.min(1, NumberOfStars.values().length - 1)
                ];

        Hotel hotel = new Hotel();
        hotel.setName("Old Hotel");
        hotel.setAddress("Old Address");
        hotel.setNumberOfStars(oldStars);
        hotel.setBasePricePerNight(new BigDecimal("100.00"));

        HotelUpdateDto hotelUpdateDto = new HotelUpdateDto();
        hotelUpdateDto.setName("New Hotel");
        hotelUpdateDto.setAddress("New Address");
        hotelUpdateDto.setNumberOfStars(newStars);
        hotelUpdateDto.setBasePricePerNight(new BigDecimal("150.00"));

        // when
        hotelMapper.updateEntity(hotelUpdateDto, hotel);

        // then
        assertEquals("New Hotel", hotel.getName());
        assertEquals("New Address", hotel.getAddress());
        assertEquals(newStars, hotel.getNumberOfStars());
        assertEquals(new BigDecimal("150.00"), hotel.getBasePricePerNight());
    }

    @Test
    void shouldNotUpdateHotelFieldsWhenValuesAreNull() {
        // given
        NumberOfStars stars = NumberOfStars.values()[0];

        Hotel hotel = new Hotel();
        hotel.setName("Central Hotel");
        hotel.setAddress("Main Street 10");
        hotel.setNumberOfStars(stars);
        hotel.setBasePricePerNight(new BigDecimal("120.00"));

        HotelUpdateDto hotelUpdateDto = new HotelUpdateDto();

        // when
        hotelMapper.updateEntity(hotelUpdateDto, hotel);

        // then
        assertEquals("Central Hotel", hotel.getName());
        assertEquals("Main Street 10", hotel.getAddress());
        assertEquals(stars, hotel.getNumberOfStars());
        assertEquals(new BigDecimal("120.00"), hotel.getBasePricePerNight());
    }

    @Test
    void shouldMapHotelsToShortDtoList() {
        // given
        City city = new City();
        city.setId(1L);
        city.setName("Madrid");

        Hotel firstHotel = new Hotel();
        firstHotel.setId(10L);
        firstHotel.setName("First Hotel");
        firstHotel.setCity(city);
        firstHotel.setAddress("First Street 1");

        Hotel secondHotel = new Hotel();
        secondHotel.setId(20L);
        secondHotel.setName("Second Hotel");
        secondHotel.setCity(city);
        secondHotel.setAddress("Second Street 2");

        List<Hotel> hotels = List.of(firstHotel, secondHotel);

        // when
        List<HotelShortDto> result = hotelMapper.toShortDtoList(hotels);

        // then
        assertEquals(2, result.size());

        assertEquals(10L, result.get(0).getId());
        assertEquals("First Hotel", result.get(0).getName());

        assertEquals(20L, result.get(1).getId());
        assertEquals("Second Hotel", result.get(1).getName());
    }
}