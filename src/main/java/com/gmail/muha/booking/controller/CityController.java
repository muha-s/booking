package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.city.CityCreateDto;
import com.gmail.muha.booking.dto.city.CityShortDto;
import com.gmail.muha.booking.service.city.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping
    public List<CityShortDto> findAll() {
        return cityService.findAll();
    }

    @PostMapping
    public CityShortDto create(@Valid @RequestBody CityCreateDto cityCreateDto) {
        return cityService.create(cityCreateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        cityService.deleteById(id);
    }
}