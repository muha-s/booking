package com.gmail.muha.booking.entity;

import com.gmail.muha.booking.entity.enums.RoomType;

import java.util.Objects;

public class Room {

    private Long id;
    private Long hotelId;
    private RoomType roomType;
    private double price;

    public Room() {

    }

    public Room(Long hotelId, RoomType roomType, double price) {
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Room room = (Room) object;
        return Double.compare(price, room.price) == 0 && Objects.equals(id, room.id) && Objects.equals(hotelId, room.hotelId) && roomType == room.roomType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hotelId, roomType, price);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", hotelId=" + hotelId +
                ", roomType=" + roomType +
                ", price=" + price +
                '}';
    }

    public String toJson() {
        return "{"
                + "\"id\":" + id + ","
                + "\"hotelId\":\"" + hotelId + "\","
                + "\"roomType\":\"" + roomType + "\","
                + "\"price\":\"" + price + "\","
                + "}";
    }
}
