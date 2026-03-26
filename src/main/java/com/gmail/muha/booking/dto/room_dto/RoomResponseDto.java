package com.gmail.muha.booking.dto.room_dto;

import com.gmail.muha.booking.entity.enums.RoomType;

public class RoomResponseDto {

    private Long id;
    private String hotelName;
    private Long hotelId;
    private RoomType roomType;
    private double price;

    public RoomResponseDto(){
    }

    public RoomResponseDto(Long id, String hotelName, Long hotelId, RoomType roomType, double price) {
        this.id = id;
        this.hotelName = hotelName;
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
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

    public String toJson() {
        return "{"
                + "\"id\":" + id + ","
                + "\"hotelName\":\"" + hotelName + "\","
                + "\"hotelId\":\"" + hotelId + "\","
                + "\"roomType\":\"" + roomType + "\","
                + "\"price\":\"" + price + "\","
                + "}";
    }
}
