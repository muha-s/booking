package com.gmail.muha.booking.dto.city_dto;

public class CityCreateDto {

    private String name;

    public CityCreateDto(){

    }

    public CityCreateDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
