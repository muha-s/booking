--liquibase formatted sql

--changeset muha:1.1-add-hotel-unique-constraint

ALTER TABLE hotels
    ADD CONSTRAINT uk_hotel_name_city
    UNIQUE (name, city_id);