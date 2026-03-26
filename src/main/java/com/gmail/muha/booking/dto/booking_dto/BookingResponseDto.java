package com.gmail.muha.booking.dto.booking_dto;

import com.gmail.muha.booking.entity.enums.RoomType;

import java.sql.Date;

public class BookingResponseDto {

    private Long id;
    private String userName;
    private RoomType roomType;
    private String hotelName;
    private Date startDate;
    private Date endDate;

    public BookingResponseDto(){
    }

    public BookingResponseDto(Long id, String userName, RoomType roomType, String hotelName, Date startDate, Date endDate) {
        this.id = id;
        this.userName = userName;
        this.roomType = roomType;
        this.hotelName = hotelName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
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

    public String toJson() {
        return "{"
                + "\"id\":" + id + ","
                + "\"userName\":\"" + userName + "\","
                + "\"roomType\":\"" + roomType + "\","
                + "\"hotelName\":\"" + hotelName + "\","
                + "\"startDate\":\"" + startDate + "\""
                + "\"endDate\":\"" + endDate + "\""
                + "}";
    }
}
