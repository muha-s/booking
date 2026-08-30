package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.HotelReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelReviewRepository extends JpaRepository<HotelReview, Long> {

    @Query("""
            select count(review) > 0
            from HotelReview review
            where review.booking.id = :bookingId
            """)
    boolean existsByBookingId(@Param("bookingId") Long bookingId);


    @Query("""
            select avg(review.score)
            from HotelReview review
            where review.booking.room.hotel.id = :hotelId
              and review.score is not null
            """)
    Double findAverageScoreByHotelId(@Param("hotelId") Long hotelId);

    @Query("""
            select count(review)
            from HotelReview review
            where review.booking.room.hotel.id = :hotelId
              and review.comment is not null
              and trim(review.comment) <> ''
            """)
    Long countCommentsByHotelId(@Param("hotelId") Long hotelId);

    @Query("""
            select review
            from HotelReview review
            where review.booking.room.hotel.id = :hotelId
              and review.comment is not null
              and trim(review.comment) <> ''
            order by review.createdAt desc
            """)
    List<HotelReview> findCommentsByHotelId(@Param("hotelId") Long hotelId);
}
