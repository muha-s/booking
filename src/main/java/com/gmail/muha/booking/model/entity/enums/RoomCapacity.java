package com.gmail.muha.booking.model.entity.enums;

public enum RoomCapacity {

    ONE_SEAT(1.0),
    TWO_SEAT(1.25),
    THREE_SEAT(1.5),
    FOUR_SEAT(1.75);

    private final Double costFactor;

    RoomCapacity(Double costFactor) {
        this.costFactor = costFactor;
    }

    public Double getCostFactor() {
        return costFactor;
    }
}
