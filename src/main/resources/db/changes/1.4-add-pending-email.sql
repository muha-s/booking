--liquibase formatted sql

--changeset muha:1.4-add-pending-email

ALTER TABLE users
    ADD COLUMN pending_email VARCHAR(128);

ALTER TABLE users
    ADD CONSTRAINT uk_users_pending_email
        UNIQUE (pending_email);