package com.gmail.muha.booking.service.impl;


import com.gmail.muha.booking.dto.hotel.HotelCreateDto;
import com.gmail.muha.booking.dto.hotel.HotelFullDto;
import com.gmail.muha.booking.dto.hotel.HotelShortDto;
import com.gmail.muha.booking.dto.hotel.HotelUpdateDto;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.HotelMapper;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.City;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.repository.BookingRepository;
import com.gmail.muha.booking.model.repository.HotelRepository;
import com.gmail.muha.booking.service.BookingCancellationService;
import com.gmail.muha.booking.service.CityService;
import com.gmail.muha.booking.service.HotelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final HotelMapper hotelMapper;
    private final CityService cityService;
    private final BookingCancellationService bookingCancellationService;


    @Override
    public HotelFullDto findById(Long id) {
        return hotelMapper.toFullDto(findEntityById(id));
    }

    @Override
    public Hotel findEntityById(Long id) {
        return hotelRepository.findActiveById(id)
                .orElseThrow(() ->
                        new NotFoundException("Hotel was not found by id: " + id));
    }

    @Override
    public List<HotelShortDto> findAll() {
        return hotelMapper.toShortDtoList(hotelRepository.findAllActive());
    }

    @Override
    public HotelFullDto create(HotelCreateDto hotelCreateDto) {
        City hotelCity = cityService.findEntityById(hotelCreateDto.getCityId());
        Hotel savedHotel = hotelRepository.save(hotelMapper.toEntity(hotelCreateDto, hotelCity));
        return hotelMapper.toFullDto(savedHotel);
    }

    @Override
    public HotelFullDto update(Long id, HotelUpdateDto hotelUpdateDto) {

        Hotel updatingHotel = findEntityById(id);
        hotelMapper.updateEntity(hotelUpdateDto, updatingHotel);
        return hotelMapper.toFullDto(hotelRepository.save(updatingHotel));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        List<Booking> bookings = bookingRepository.findFutureActiveBookingsByHotelId(id);

        for (Booking booking : bookings) {
            bookingCancellationService.cancelByAdministration(booking);
        }
        Hotel hotel = findEntityById(id);
        hotel.setDeletedAt(Instant.now());
    }
}
