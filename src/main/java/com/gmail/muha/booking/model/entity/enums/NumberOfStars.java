package com.gmail.muha.booking.model.entity.enums;

public enum NumberOfStars {

    ONE_STAR("1"), TWO_STARS("2"), THREE_STARS("3"), THREE_STARS_PLUS("3+"), FOUR_STARS("4"), FOUR_STARS_PLUS("4+"),
    FIVE_STARS("5"), FIVE_STARS_PLUS("5+");

    private final String numberOfStars;

    NumberOfStars(String numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public String getNumberOfStars() {
        return numberOfStars;
    }
}
