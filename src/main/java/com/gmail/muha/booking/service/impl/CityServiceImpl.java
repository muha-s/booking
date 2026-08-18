package com.gmail.muha.booking.service.impl;

import com.gmail.muha.booking.dto.city.CityCreateDto;
import com.gmail.muha.booking.dto.city.CityFullDto;
import com.gmail.muha.booking.dto.city.CityShortDto;
import com.gmail.muha.booking.dto.city.CityUpdateDto;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.CityMapper;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.City;
import com.gmail.muha.booking.model.repository.BookingRepository;
import com.gmail.muha.booking.model.repository.CityRepository;
import com.gmail.muha.booking.service.BookingCancellationService;
import com.gmail.muha.booking.service.CityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final BookingRepository bookingRepository;
    private final CityMapper cityMapper;
    private final BookingCancellationService bookingCancellationService;

    @Override
    public CityFullDto findById(Long id) {
        return cityMapper.toFullDto(findEntityById(id));
    }

    @Override
    public City findEntityById(Long id) {
        return cityRepository.findActiveById(id)
                .orElseThrow(() ->
                        new NotFoundException("City was not found by id: " + id));
    }

    @Override
    public List<CityShortDto> findAll() {
        return cityMapper.toShortDtoList(cityRepository.findAllActive());
    }

    @Override
    public CityFullDto create(CityCreateDto cityCreateDto) {
        City savedCity = cityRepository.save(cityMapper.toEntity(cityCreateDto));
        return cityMapper.toFullDto(savedCity);
    }

    @Override
    public CityFullDto update(Long id, CityUpdateDto cityUpdateDto) {

        City updatingCity = findEntityById(id);
        cityMapper.updateEntity(cityUpdateDto, updatingCity);
        return cityMapper.toFullDto(cityRepository.save(updatingCity));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {

        List<Booking> bookings = bookingRepository.findFutureActiveBookingsByCityId(id);

        for (Booking booking : bookings) {
            bookingCancellationService.cancelByAdministration(booking);
        }
        City city = findEntityById(id);
        city.setDeletedAt(Instant.now());
    }
}
