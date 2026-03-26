package com.gmail.muha.booking.dto.booking_dto;

import java.sql.Date;
import java.time.LocalDate;

public class BookingCreateDto {

    private Long userId;
    private Long roomId;
    private Date startDate;
    private Date endDate;

    public BookingCreateDto(){

    }

    public BookingCreateDto(Long userId, Long roomId, Date startDate, Date endDate) {
        this.userId = userId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
