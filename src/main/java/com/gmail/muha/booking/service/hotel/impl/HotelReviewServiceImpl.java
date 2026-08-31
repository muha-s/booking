package com.gmail.muha.booking.service.hotel.impl;

import com.gmail.muha.booking.dto.hotel_review.*;
import com.gmail.muha.booking.exception.EmailSendingException;
import com.gmail.muha.booking.exception.HotelReviewException;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.HotelReviewMapper;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.HotelReview;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import com.gmail.muha.booking.model.repository.HotelReviewRepository;
import com.gmail.muha.booking.service.booking.BookingService;
import com.gmail.muha.booking.service.email.EmailService;
import com.gmail.muha.booking.service.hotel.HotelReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelReviewServiceImpl implements HotelReviewService {

    private final HotelReviewRepository hotelReviewRepository;
    private final HotelReviewMapper hotelReviewMapper;
    private final BookingService bookingService;
    private final EmailService emailService;


    @Transactional
    @Override
    public HotelReviewResponseDto create(HotelReviewCreateDto hotelReviewCreateDto, String userEmail) {

        if (hotelReviewCreateDto.getScore() == null
                && (hotelReviewCreateDto.getComment() == null
                || hotelReviewCreateDto.getComment().isBlank())) {
            throw new HotelReviewException("Review must contain a score or comment");
        }

        Booking reviewBooking = bookingService.findEntityByIdForUser(hotelReviewCreateDto.getBookingId(), userEmail);

        if (reviewBooking.getStatus() != BookingStatus.COMPLETED) {
            throw new HotelReviewException("Review can only be created for a completed booking");
        }

        if (hotelReviewRepository.existsByBookingId(reviewBooking.getId())) {
            throw new HotelReviewException("Review already exists for this booking");
        }

        HotelReview savedReview =
                hotelReviewRepository.save(hotelReviewMapper.toEntity(hotelReviewCreateDto, reviewBooking));

        Hotel reviewedHotel = reviewBooking.getRoom().getHotel();

        Double averageRating = hotelReviewRepository.findAverageScoreByHotelId(reviewedHotel.getId());

        if (averageRating != null) {
            double roundedRating = Math.round(averageRating * 10.0) / 10.0;
            reviewedHotel.setRating(roundedRating);
        }
        return hotelReviewMapper.toHotelReviewResponseDto(savedReview);
    }

    @Scheduled(cron = "0 45 16 * * *")
    @Transactional
    @Override
    public void sendReviewRequests() {

        List<Booking> bookingsForReview = bookingService.findBookingsForReviewRequest(BookingStatus.COMPLETED);

        for (Booking booking : bookingsForReview) {

            String reviewUrl = "http://localhost:4200/review/" + booking.getId();

            try {
                emailService.sendEmail(
                        booking.getUser().getEmail(),
                        "Оставьте отзыв о проживании",
                        """
                                Спасибо, что воспользовались нашим сервисом.
                                
                                Вы можете оставить отзыв об отеле и поставить оценку по ссылке:
                                
                                %s
                                """.formatted(reviewUrl));

                booking.setReviewRequestSentAt(Instant.now());

            } catch (EmailSendingException exception) {
                //TODO log
            }
        }
    }

    @Override
    public Long countCommentsByHotelId(Long hotelId) {
        return hotelReviewRepository.countCommentsByHotelId(hotelId);
    }

    @Override
    public List<HotelReviewForHotelDto> findCommentsByHotelId(Long hotelId) {

        return hotelReviewRepository.findCommentsByHotelId(hotelId)
                .stream()
                .map(hotelReviewMapper::toHotelReviewForHotelDto)
                .toList();
    }

}
