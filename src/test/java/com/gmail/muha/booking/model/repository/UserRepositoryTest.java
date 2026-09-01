package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = "spring.liquibase.enabled=false")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindActiveUserById() {
        // given
        User user = createUser("user1@test.com", UserRole.USER, null);

        // when
        Optional<User> result = userRepository.findActiveById(user.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
        assertEquals("user1@test.com", result.get().getEmail());
    }

    @Test
    void shouldNotFindDeletedUserById() {
        // given
        User user = createUser("user2@test.com", UserRole.USER, Instant.now());

        // when
        Optional<User> result = userRepository.findActiveById(user.getId());

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindActiveUserByEmail() {
        // given
        User user = createUser("user3@test.com", UserRole.USER, null);

        // when
        Optional<User> result = userRepository.findActiveByEmail("user3@test.com");

        // then
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
    }

    @Test
    void shouldNotFindDeletedUserByEmail() {
        // given
        createUser("user4@test.com", UserRole.USER, Instant.now());

        // when
        Optional<User> result = userRepository.findActiveByEmail("user4@test.com");

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindAllActiveUsersByRole() {
        // given
        User activeUser1 = createUser("user5@test.com", UserRole.USER, null);

        User activeUser2 = createUser("user6@test.com", UserRole.USER, null);

        createUser("user7@test.com", UserRole.USER, Instant.now());

        createUser("admin1@test.com", UserRole.HOTEL_ADMIN, null);

        // when
        List<User> result =
                userRepository.findAllActiveByRole(UserRole.USER);

        // then
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(user -> user.getId().equals(activeUser1.getId())));

        assertTrue(result.stream().anyMatch(user -> user.getId().equals(activeUser2.getId())));
    }

    @Test
    void shouldFindDeletedUserByEmailIgnoringCase() {
        // given
        User deletedUser = createUser("DeletedUser@Test.com", UserRole.USER, Instant.now());

        // when
        Optional<User> result = userRepository.findDeletedByEmail("deleteduser@test.com");

        // then
        assertTrue(result.isPresent());
        assertEquals(deletedUser.getId(), result.get().getId());
    }

    @Test
    void shouldNotFindActiveUserAsDeleted() {
        // given
        createUser("active@test.com", UserRole.USER, null);

        // when
        Optional<User> result = userRepository.findDeletedByEmail("active@test.com");

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCheckIfEmailExistsIgnoringCase() {
        // given
        createUser("ExistingUser@Test.com", UserRole.USER, null);

        // when
        boolean existing = userRepository.existsByEmailIgnoreCase("existinguser@test.com");

        boolean missing = userRepository.existsByEmailIgnoreCase("missing@test.com");

        // then
        assertTrue(existing);
        assertFalse(missing);
    }

    @Test
    void shouldCheckIfPendingEmailExistsIgnoringCase() {
        // given
        User user = createUser("user8@test.com", UserRole.USER, null);

        user.setPendingEmail("NewEmail@Test.com");
        userRepository.save(user);

        // when
        boolean existing = userRepository.existsByPendingEmailIgnoreCase("newemail@test.com");

        boolean missing = userRepository.existsByPendingEmailIgnoreCase("other@test.com");

        // then
        assertTrue(existing);
        assertFalse(missing);
    }

    @Test
    void shouldFindAllUsersByRoleOrderedByIdAscending() {
        // given
        User firstAdmin = createUser("admin2@test.com", UserRole.HOTEL_ADMIN, null);

        User secondAdmin = createUser("admin3@test.com", UserRole.HOTEL_ADMIN, Instant.now());

        User thirdAdmin = createUser("admin4@test.com", UserRole.HOTEL_ADMIN, null);

        createUser("user9@test.com", UserRole.USER, null);

        // when
        List<User> result = userRepository.findAllByRoleOrderByIdAsc(UserRole.HOTEL_ADMIN);

        // then
        assertEquals(3, result.size());

        assertEquals(firstAdmin.getId(), result.get(0).getId());
        assertEquals(secondAdmin.getId(), result.get(1).getId());
        assertEquals(thirdAdmin.getId(), result.get(2).getId());
    }

    private User createUser(String email, UserRole role, Instant deletedAt) {
        User user = new User();

        user.setRole(role);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhone("+34123456789");
        user.setEmail(email);
        user.setPassword("password");
        user.setBalance(new BigDecimal("1000.00"));
        user.setEmailVerified(true);
        user.setDeletedAt(deletedAt);

        return userRepository.save(user);
    }
}