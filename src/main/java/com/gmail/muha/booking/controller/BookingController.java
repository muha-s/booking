package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.booking.BookingCreateDto;
import com.gmail.muha.booking.dto.booking.BookingFullDto;
import com.gmail.muha.booking.dto.booking.BookingShortDto;
import com.gmail.muha.booking.dto.booking.BookingUpdateDto;
import com.gmail.muha.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    @GetMapping
    public List<BookingShortDto> findAll() {
        return bookingService.findAll();
    }

    @GetMapping("/{id}")
    public BookingFullDto findById(@PathVariable Long id) {
        return bookingService.findById(id);
    }

    @PostMapping
    public BookingFullDto create(@Valid @RequestBody BookingCreateDto bookingCreateDto) {
        return bookingService.create(bookingCreateDto);
    }

    @PutMapping("/{id}")
    public BookingFullDto update(@PathVariable Long id, @Valid @RequestBody BookingUpdateDto bookingUpdateDto) {
        return bookingService.update(id, bookingUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        bookingService.deleteById(id);
    }
}
