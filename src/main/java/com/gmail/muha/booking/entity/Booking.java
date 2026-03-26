package com.gmail.muha.booking.entity;

import java.sql.Date;
import java.util.Objects;

public class Booking {

    private Long id;
    private Long userId;
    private Long roomId;
    private Date startDate;
    private Date endDate;


    public Booking() {

    }

    public Booking(Long userId, Long roomId, Date startDate, Date endDate) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", userId=" + userId +
                ", roomId=" + roomId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public String toJson() {
        return "{"
                + "\"id\":" + id + ","
                + "\"userId\":\"" + userId + "\","
                + "\"roomId\":\"" + roomId + "\","
                + "\"startDate\":\"" + startDate + "\","
                + "\"endDate\":\"" + endDate + "\""
                + "}";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Booking booking = (Booking) object;
        return Objects.equals(id, booking.id) && Objects.equals(userId, booking.userId) && Objects.equals(roomId, booking.roomId) && Objects.equals(startDate, booking.startDate) && Objects.equals(endDate, booking.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, roomId, startDate, endDate);
    }
}
