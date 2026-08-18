package com.gmail.muha.booking.service.impl;

import com.gmail.muha.booking.dto.hotel_review.HotelReviewCreateDto;
import com.gmail.muha.booking.dto.hotel_review.HotelReviewDto;
import com.gmail.muha.booking.dto.hotel_review.HotelReviewUpdateDto;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.HotelReviewMapper;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.HotelReview;
import com.gmail.muha.booking.model.repository.HotelReviewRepository;
import com.gmail.muha.booking.service.BookingService;
import com.gmail.muha.booking.service.HotelReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelReviewServiceImpl implements HotelReviewService {

    private final HotelReviewRepository hotelReviewRepository;
    private final HotelReviewMapper hotelReviewMapper;
    private final BookingService bookingService;


    @Override
    public HotelReviewDto findById(Long id) {
        return hotelReviewMapper.toDto(findEntityById(id));
    }

    @Override
    public HotelReview findEntityById(Long id) {
        return hotelReviewRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Hotel review was not found by id: " + id));
    }

    @Override
    public List<HotelReviewDto> findAll() {
        return hotelReviewMapper.toDtoList(hotelReviewRepository.findAll());
    }

    @Override
    public HotelReviewDto create(HotelReviewCreateDto hotelReviewCreateDto) {
        Booking hotelReviewBooking = bookingService.findEntityById(hotelReviewCreateDto.getBookingId());
        HotelReview savedHotelReview =
                hotelReviewRepository.save(hotelReviewMapper.toEntity(hotelReviewCreateDto, hotelReviewBooking));
        return hotelReviewMapper.toDto(savedHotelReview);
    }

    @Override
    public HotelReviewDto update(Long id, HotelReviewUpdateDto hotelReviewUpdateDto) {

        HotelReview updatingHotelReview = findEntityById(id);
        hotelReviewMapper.updateEntity(hotelReviewUpdateDto, updatingHotelReview);
        return hotelReviewMapper.toDto(hotelReviewRepository.save(updatingHotelReview));
    }

    @Override
    public void deleteById(Long id) {
        hotelReviewRepository.deleteById(id);
    }

}
