package com.gmail.muha.booking.dto.room_dto;

import com.gmail.muha.booking.entity.enums.RoomType;

import java.util.Objects;

public class RoomUpdateDto {

    private Long id;
    //private Long hotelId;
    private RoomType roomType;
    private double price;

    private RoomUpdateDto(){

    }

//    public RoomUpdateDto(Long id, Long hotelId, RoomType roomType, double price) {
//        this.id = id;
//        this.hotelId = hotelId;
//        this.roomType = roomType;
//        this.price = price;
//    }

    public RoomUpdateDto(Long id, RoomType roomType, double price) {
        this.id = id;
        this.roomType = roomType;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        RoomUpdateDto that = (RoomUpdateDto) object;
        return Double.compare(price, that.price) == 0 && Objects.equals(id, that.id) && roomType == that.roomType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomType, price);
    }

    @Override
    public String toString() {
        return "RoomUpdateDto{" +
                "id=" + id +
                ", roomType=" + roomType +
                ", price=" + price +
                '}';
    }

    public String toJson() {
        return "{"
                + "\"roomType\":\"" + roomType + "\","
                + "\"price\":\"" + price + "\","
                + "}";
    }
}
