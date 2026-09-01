package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.booking.BookingCreateDto;
import com.gmail.muha.booking.dto.booking.BookingForReviewDto;
import com.gmail.muha.booking.dto.booking.BookingForUserDto;
import com.gmail.muha.booking.dto.booking.BookingUpdateDto;
import com.gmail.muha.booking.service.booking.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    @GetMapping("/my")
    public Page<BookingForUserDto> findMyBookings(
            Authentication authentication, @PageableDefault(size = 10) Pageable pageable) {
        return bookingService.findAllByUserEmail(authentication.getName(), pageable);
    }

    @PostMapping
    public void create(@Valid @RequestBody BookingCreateDto bookingCreateDto, Authentication authentication) {
        bookingService.create(bookingCreateDto, authentication.getName());
    }

    @DeleteMapping("/my/{id}")
    public void cancelMyBooking(@PathVariable Long id, Authentication authentication) {
        bookingService.cancelByUser(id, authentication.getName());
    }

    @PutMapping("/my/{id}")
    public void updateMyBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingUpdateDto bookingUpdateDto,
            Authentication authentication) {

        bookingService.updateForUser(id, bookingUpdateDto, authentication.getName());
    }

    @GetMapping("/my/{id}/review")
    public BookingForReviewDto findMyBookingForReview(@PathVariable Long id, Authentication authentication) {
        return bookingService.findForReview(id, authentication.getName());
    }
}
