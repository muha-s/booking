package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.booking.*;
import com.gmail.muha.booking.service.booking.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{id}")
    public BookingFullDto findById(@PathVariable Long id) {

        return bookingService.findById(id);
    }

    @GetMapping
    public List<BookingShortDto> findAll() {
        return bookingService.findAll();
    }

    @GetMapping("/my/{id}")
    public BookingFullDto findMyBookingById(@PathVariable Long id, Authentication authentication) {
        return bookingService.findByIdForUser(id, authentication.getName());
    }

    @GetMapping("/my")
    public List<BookingForUserDto> findMyBookings(Authentication authentication) {
        return bookingService.findAllByUserEmail(authentication.getName());
    }

    @PostMapping
    public BookingFullDto create(@Valid @RequestBody BookingCreateDto bookingCreateDto, Authentication authentication) {

        return bookingService.create(bookingCreateDto, authentication.getName());
    }

    @PutMapping("/{id}")
    public BookingFullDto update(@PathVariable Long id, @Valid @RequestBody BookingUpdateDto bookingUpdateDto) {
        return bookingService.update(id, bookingUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        bookingService.deleteById(id);
    }

    @DeleteMapping("/my/{id}")
    public void cancelMyBooking(@PathVariable Long id, Authentication authentication) {
        bookingService.cancelByUser(id, authentication.getName());
    }

    @PutMapping("/my/{id}")
    public BookingFullDto updateMyBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingUpdateDto bookingUpdateDto,
            Authentication authentication) {

        return bookingService.updateForUser(id, bookingUpdateDto, authentication.getName());
    }

    @GetMapping("/my/{id}/review")
    public BookingForReviewDto findMyBookingForReview(@PathVariable Long id, Authentication authentication) {
        return bookingService.findForReview(id, authentication.getName());
    }
}
