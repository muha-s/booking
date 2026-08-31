package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.hotel.HotelCreateDto;
import com.gmail.muha.booking.dto.hotel.HotelShortDto;
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

    @PostMapping
    public HotelShortDto create(@Valid @RequestBody HotelCreateDto hotelCreateDto) {
        return hotelService.create(hotelCreateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        hotelService.deleteById(id);
    }
}
