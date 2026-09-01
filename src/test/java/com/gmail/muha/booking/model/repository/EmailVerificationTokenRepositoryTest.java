package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.EmailVerificationToken;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = "spring.liquibase.enabled=false")
class EmailVerificationTokenRepositoryTest {

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindTokenByTokenValue() {
        // given
        User user = createUser("user1@test.com");

        EmailVerificationToken verificationToken =
                createToken(user, "token-123");

        // when
        Optional<EmailVerificationToken> result =
                emailVerificationTokenRepository.findByToken("token-123");

        // then
        assertTrue(result.isPresent());
        assertEquals(verificationToken.getId(), result.get().getId());
        assertEquals("token-123", result.get().getToken());
    }

    @Test
    void shouldReturnEmptyWhenTokenDoesNotExist() {
        // when
        Optional<EmailVerificationToken> result =
                emailVerificationTokenRepository.findByToken("missing-token");

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindTokenByUserId() {
        // given
        User user = createUser("user2@test.com");

        EmailVerificationToken verificationToken =
                createToken(user, "token-456");

        // when
        Optional<EmailVerificationToken> result =
                emailVerificationTokenRepository.findByUser_Id(user.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(verificationToken.getId(), result.get().getId());
        assertEquals(user.getId(), result.get().getUser().getId());
    }

    @Test
    void shouldReturnEmptyWhenUserHasNoToken() {
        // given
        User user = createUser("user3@test.com");

        // when
        Optional<EmailVerificationToken> result =
                emailVerificationTokenRepository.findByUser_Id(user.getId());

        // then
        assertTrue(result.isEmpty());
    }

    private User createUser(String email) {
        User user = new User();

        user.setRole(UserRole.USER);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhone("+34123456789");
        user.setEmail(email);
        user.setPassword("password");
        user.setBalance(new BigDecimal("1000.00"));
        user.setEmailVerified(false);

        return userRepository.save(user);
    }

    private EmailVerificationToken createToken(
            User user,
            String token
    ) {
        EmailVerificationToken verificationToken =
                new EmailVerificationToken();

        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(
                Instant.now().plusSeconds(3600)
        );

        return emailVerificationTokenRepository.save(
                verificationToken
        );
    }
}