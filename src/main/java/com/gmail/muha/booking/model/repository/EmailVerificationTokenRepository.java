package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByToken(String token);

    void deleteAllByUserId(Long userId);

    Optional<EmailVerificationToken> findByUser_Id(Long userId);
}