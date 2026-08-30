package com.gmail.muha.booking.service.email.impl;

import com.gmail.muha.booking.model.entity.EmailVerificationToken;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.repository.EmailVerificationTokenRepository;
import com.gmail.muha.booking.service.email.EmailVerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

    private final EmailVerificationTokenRepository tokenRepository;

    @Override
    @Transactional
    public EmailVerificationToken create(User user) {

        EmailVerificationToken verificationToken = tokenRepository.findByUser_Id(user.getId())
                .orElseGet(EmailVerificationToken::new);

        verificationToken.setUser(user);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));

        return tokenRepository.save(verificationToken);
    }

    @Override
    public EmailVerificationToken findValidByToken(String token) {

        EmailVerificationToken verificationToken =
                tokenRepository.findByToken(token).orElseThrow(() ->
                                new IllegalArgumentException("Invalid verification token"));

        if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("Verification token has expired");
        }
        return verificationToken;
    }

    @Override
    @Transactional
    public void delete(EmailVerificationToken verificationToken) {
        tokenRepository.delete(verificationToken);
    }

}