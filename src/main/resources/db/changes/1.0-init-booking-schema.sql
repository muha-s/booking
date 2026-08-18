-- liquibase formatted sql


-- changeset muha:1
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT,
    role VARCHAR(64) NOT NULL,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    phone VARCHAR(32) NOT NULL,
    email VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    balance DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    deleted_at DATETIME,
    PRIMARY KEY (id)
);


-- changeset muha:2
CREATE TABLE cities (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL UNIQUE,
    deleted_at DATETIME,
    PRIMARY KEY (id)
);


-- changeset muha:3
CREATE TABLE hotels (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    city_id BIGINT NOT NULL,
    address VARCHAR(255) NOT NULL,
    number_of_stars VARCHAR(64) NOT NULL,
    rating DOUBLE NOT NULL DEFAULT 0.0,
    base_price_per_night DECIMAL(12,2) NOT NULL,
    balance DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    deleted_at DATETIME,

    PRIMARY KEY (id),

    CONSTRAINT fk_hotels_cities
        FOREIGN KEY (city_id)
        REFERENCES cities(id)
);


-- changeset muha:4
CREATE TABLE rooms (
    id BIGINT AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    room_capacity VARCHAR(64) NOT NULL,
    room_type VARCHAR(64) NOT NULL,
    deleted_at DATETIME,

    PRIMARY KEY (id),

    CONSTRAINT fk_rooms_hotels
        FOREIGN KEY (hotel_id)
        REFERENCES hotels(id)
);


-- changeset muha:5
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(64) NOT NULL DEFAULT 'ACTIVE',
    total_price DECIMAL(12,2) NOT NULL,
    review_request_sent_at DATETIME,

    PRIMARY KEY (id),

    CONSTRAINT fk_bookings_users
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_bookings_rooms
        FOREIGN KEY (room_id)
        REFERENCES rooms(id)
);


-- changeset muha:6
CREATE TABLE hotel_reviews (
    id BIGINT AUTO_INCREMENT,
    booking_id BIGINT NOT NULL UNIQUE,
    rating DOUBLE NOT NULL,
    comment VARCHAR(2000),
    created_at DATETIME NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_hotel_reviews_bookings
        FOREIGN KEY (booking_id)
        REFERENCES bookings(id)
);


-- changeset muha:7
CREATE TABLE hotels_admins (
    hotel_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    PRIMARY KEY (hotel_id, user_id),

    CONSTRAINT fk_hotels_admins_hotels
        FOREIGN KEY (hotel_id)
        REFERENCES hotels(id),

    CONSTRAINT fk_hotels_admins_users
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);