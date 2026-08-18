package com.gmail.muha.booking.model.entity.enums;

public enum RoomType {

    STANDARD(1.0),
    COMFORT(1.3),
    LUXURY(1.7);

    private final Double costFactor;

    RoomType(Double costFactor) {
        this.costFactor = costFactor;
    }

    public Double getCostFactor() {
        return costFactor;
    }
}
