package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.city.CityCreateDto;
import com.gmail.muha.booking.dto.city.CityFullDto;
import com.gmail.muha.booking.dto.city.CityShortDto;
import com.gmail.muha.booking.dto.city.CityUpdateDto;
import com.gmail.muha.booking.service.CityService;
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

    @GetMapping("/{id}")
    public CityFullDto findById(@PathVariable Long id) {
        return cityService.findById(id);
    }

    @PostMapping
    public CityFullDto create(@Valid @RequestBody CityCreateDto cityCreateDto) {
        return cityService.create(cityCreateDto);
    }

    @PutMapping("/{id}")
    public CityFullDto update(@PathVariable Long id, @Valid @RequestBody CityUpdateDto cityUpdateDto) {
        return cityService.update(id, cityUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        cityService.deleteById(id);
    }
}