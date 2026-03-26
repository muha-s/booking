package com.gmail.muha.booking.dto.booking_dto;

import java.sql.Date;
import java.util.Objects;

public class BookingUpdateDto {

    private Long id;
    private Long roomId;
    private Date startDate;
    private Date endDate;


    public BookingUpdateDto() {
    }

    public BookingUpdateDto(Long id, Long roomId, Date startDate, Date endDate) {
        this.id = id;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        BookingUpdateDto that = (BookingUpdateDto) object;
        return Objects.equals(id, that.id) && Objects.equals(roomId, that.roomId) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomId, startDate, endDate);
    }

    @Override
    public String toString() {
        return "BookingUpdateDto{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public String toJson() {
        return "{"
                + "\"roomId\":\"" + roomId + "\","
                + "\"startDate\":\"" + startDate + "\","
                + "\"endDate\":\"" + endDate + "\","
                + "}";
    }
}
