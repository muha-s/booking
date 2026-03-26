package com.gmail.muha.booking.entity;

import java.util.Objects;

public class Hotel {

    private Long id;
    private String name;
    private Long cityId;

    public Hotel() {

    }

    public Hotel(String name, Long cityId) {
        this.name = name;
        this.cityId = cityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Hotel hotel = (Hotel) object;
        return Objects.equals(id, hotel.id) && Objects.equals(name, hotel.name) && Objects.equals(cityId, hotel.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cityId);
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cityId=" + cityId +
                '}';
    }

    public String toJson() {
        return "{"
                + "\"id\":" + id + ","
                + "\"name\":\"" + name + "\","
                + "\"cityId\":\"" + cityId + "\","
                + "}";
    }
}
