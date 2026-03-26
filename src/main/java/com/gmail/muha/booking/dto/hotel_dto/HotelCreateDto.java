package com.gmail.muha.booking.dto.hotel_dto;

public class HotelCreateDto {

    private String name;
    private Long cityId;

    public HotelCreateDto(){

    }

    public HotelCreateDto(String name, Long cityId) {
        this.name = name;
        this.cityId = cityId;
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
}
