package com.gmail.muha.booking.service.booking;

import com.gmail.muha.booking.dto.booking.*;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;

import java.util.List;

public interface BookingService {

    List<BookingForUserDto> findAllByUserEmail(String userEmail);

    void create(BookingCreateDto bookingCreateDto, String userEmail);

    void updateForUser(Long id, BookingUpdateDto bookingUpdateDto, String userEmail);

    Booking findEntityByIdForUser(Long id, String userEmail);

    List<Booking> findBookingsForReviewRequest(BookingStatus status);

    void cancelByUser(Long id, String userEmail);


    BookingForReviewDto findForReview(Long id, String userEmail);

    List<BookingManagedDto> findManagedByHotelId(Long hotelId);

}
