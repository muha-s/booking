package com.gmail.muha.booking.dto.room_dto;

import com.gmail.muha.booking.entity.enums.RoomType;

public class RoomCreateDto {

    private Long hotelId;
    private RoomType roomType;
    private double price;

    public RoomCreateDto(){

    }

    public RoomCreateDto(Long hotelId, RoomType roomType, double price) {
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.price = price;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
