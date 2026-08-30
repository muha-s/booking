package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.hotel.HotelCreateDto;
import com.gmail.muha.booking.dto.hotel.HotelFullDto;
import com.gmail.muha.booking.dto.hotel.HotelShortDto;
import com.gmail.muha.booking.dto.hotel.HotelUpdateDto;
import com.gmail.muha.booking.service.hotel.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public List<HotelShortDto> findAll() {
        return hotelService.findAll();
    }

    @GetMapping("/{id}")
    public HotelFullDto findById(@PathVariable Long id) {
        return hotelService.findById(id);
    }

    @PostMapping
    public HotelFullDto create(@Valid @RequestBody HotelCreateDto hotelCreateDto) {
        return hotelService.create(hotelCreateDto);
    }

    @PutMapping("/{id}")
    public HotelFullDto update(@PathVariable Long id, @Valid @RequestBody HotelUpdateDto hotelUpdateDto) {
        return hotelService.update(id, hotelUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        hotelService.deleteById(id);
    }
}
