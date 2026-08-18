package com.gmail.muha.booking.service.impl.result;

import com.gmail.muha.booking.model.entity.Room;

import java.math.BigDecimal;

public record PreparedBookingData(Room room, BigDecimal totalPrice) {

}