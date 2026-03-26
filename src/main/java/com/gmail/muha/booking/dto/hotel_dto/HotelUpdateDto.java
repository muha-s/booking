package com.gmail.muha.booking.dto.hotel_dto;

import java.util.Objects;

public class HotelUpdateDto {

    private Long id;
    private String name;


    public HotelUpdateDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public HotelUpdateDto() {
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

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        HotelUpdateDto that = (HotelUpdateDto) object;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "HotelUpdateDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public String toJson() {
        return "{"
                + "\"name\":\"" + name + "\","
                + "}";
    }
}
