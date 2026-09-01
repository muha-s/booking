package com.gmail.muha.booking.service.booking;

import com.gmail.muha.booking.dto.booking.*;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {

    Page<BookingForUserDto> findAllByUserEmail(String userEmail, Pageable pageable);

    Page<BookingManagedDto> findManagedByHotelId(Long hotelId, Pageable pageable);

    void create(BookingCreateDto bookingCreateDto, String userEmail);

    void updateForUser(Long id, BookingUpdateDto bookingUpdateDto, String userEmail);

    Booking findEntityByIdForUser(Long id, String userEmail);

    List<Booking> findBookingsForReviewRequest(BookingStatus status);

    void cancelByUser(Long id, String userEmail);


    BookingForReviewDto findForReview(Long id, String userEmail);


}
