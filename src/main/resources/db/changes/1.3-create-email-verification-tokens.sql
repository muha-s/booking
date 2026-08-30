--liquibase formatted sql

--changeset muha:1.3-create-email-verification-tokens

CREATE TABLE email_verification_tokens (
    id BIGINT AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token VARCHAR(64) NOT NULL,
    expires_at DATETIME NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_email_verification_tokens_user
        UNIQUE (user_id),

    CONSTRAINT uk_email_verification_tokens_token
        UNIQUE (token),

    CONSTRAINT fk_email_verification_tokens_users
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);