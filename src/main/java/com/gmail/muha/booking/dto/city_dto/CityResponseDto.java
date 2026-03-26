package com.gmail.muha.booking.dto.city_dto;

public class CityResponseDto {

    private Long id;
    private String name;

    public CityResponseDto(){

    }

    public CityResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public String toJson() {
        return "{"
                + "\"id\":" + id + ","
                + "\"name\":\"" + name + "\","
                + "}";
    }
}
