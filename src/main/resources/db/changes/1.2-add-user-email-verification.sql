--liquibase formatted sql

--changeset muha:1.2-add-user-email-verification

ALTER TABLE users
    ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT FALSE;