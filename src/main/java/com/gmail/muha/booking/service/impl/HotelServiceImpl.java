package com.gmail.muha.booking.service.impl;

import com.gmail.muha.booking.dto.hotel_dto.HotelCreateDto;
import com.gmail.muha.booking.dto.hotel_dto.HotelResponseDto;
import com.gmail.muha.booking.dto.hotel_dto.HotelUpdateDto;
import com.gmail.muha.booking.entity.City;
import com.gmail.muha.booking.entity.Hotel;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.HotelMapper;
import com.gmail.muha.booking.repository.HotelRepository;
import com.gmail.muha.booking.service.CityService;
import com.gmail.muha.booking.service.HotelService;

import java.util.ArrayList;
import java.util.List;

public class HotelServiceImpl implements HotelService {

    private final HotelRepository repository;
    private final HotelMapper mapper;
    private final CityService cityService;


    public HotelServiceImpl(HotelRepository repository, HotelMapper mapper, CityService cityService) {
        this.repository = repository;
        this.mapper = mapper;
        this.cityService = cityService;
    }

    @Override
    public HotelResponseDto createHotel(HotelCreateDto dto) {
        Hotel hotel = mapper.toEntity(dto);
        City city = cityService.findCityEntityById(hotel.getCityId());
        hotel = repository.save(hotel);
        return mapper.toResponseDto(hotel, city);
    }

    @Override
    public HotelResponseDto getHotel(Long id) {
        Hotel hotel = findHotelEntityById(id);

        City city = cityService.findCityEntityById(hotel.getCityId());

        return mapper.toResponseDto(hotel, city);
    }

    @Override
    public List<HotelResponseDto> getAllHotels() {
        return toDtoList(repository.findAll());
    }

    @Override
    public HotelResponseDto updateHotel(HotelUpdateDto hotelUpdateDto) {
        Hotel hotel = findHotelEntityById(hotelUpdateDto.getId());
        hotel.setName(hotelUpdateDto.getName());

        hotel = repository.update(hotel);
        City city = cityService.findCityEntityById(hotel.getCityId());

        return mapper.toResponseDto(hotel, city);
    }

    @Override
    public void deleteHotel(Long id) {
        findHotelEntityById(id);
        repository.deleteById(id);
    }

    @Override
    public Hotel findHotelEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Hotel not found with id: " + id));
    }

    private List<HotelResponseDto> toDtoList(List<Hotel> hotels) {

        List<HotelResponseDto> hotelDtoList = new ArrayList<>();

        for (Hotel hotel : hotels) {
            City city = cityService.findCityEntityById(hotel.getCityId());

            hotelDtoList.add(mapper.toResponseDto(hotel, city));
        }
        return hotelDtoList;
    }
}
