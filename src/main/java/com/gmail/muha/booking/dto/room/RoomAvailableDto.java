package com.gmail.muha.booking.dto.room;

import com.gmail.muha.booking.model.entity.enums.NumberOfStars;
import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomAvailableDto {

    private Long id;
    private Long hotelId;
    private String hotelName;
    private String hotelAddress;
    private NumberOfStars hotelStars;
    private Double hotelRating;
    private Long hotelReviewCount;
    private RoomType roomType;
    private RoomCapacity roomCapacity;
    private BigDecimal pricePerNight;
    private BigDecimal totalStayPrice;
}
