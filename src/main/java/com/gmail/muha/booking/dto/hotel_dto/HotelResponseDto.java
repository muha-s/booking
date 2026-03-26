package com.gmail.muha.booking.dto.hotel_dto;

public class HotelResponseDto {

    private Long id;
    private String name;
    private String cityName;

    public HotelResponseDto(){
    }

    public HotelResponseDto(Long id, String name, String cityName) {
        this.id = id;
        this.name = name;
        this.cityName = cityName;
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String toJson() {
        return "{"
                + "\"id\":" + id + ","
                + "\"name\":\"" + name + "\","
                + "\"cityName\":\"" + cityName + "\","
                + "}";
    }
}
