package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.user.*;
import com.gmail.muha.booking.model.entity.City;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserMapperTest {

    private final ShortDtoMapper shortDtoMapper = new ShortDtoMapper();
    private final UserMapper userMapper = new UserMapper(shortDtoMapper);

    @Test
    void shouldMapUserToUserProfileDto() {
        // given
        User user = new User();
        user.setId(1L);
        user.setRole(UserRole.USER);
        user.setFirstName("Alex");
        user.setLastName("Smith");
        user.setPhone("+34123456789");
        user.setBalance(new BigDecimal("1000.00"));

        // when
        UserProfileDto result = userMapper.toUserProfileDto(user);

        // then
        assertEquals(1L, result.getId());
        assertEquals(UserRole.USER, result.getRole());
        assertEquals("Alex", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("+34123456789", result.getPhone());
        assertEquals(new BigDecimal("1000.00"), result.getBalance());
    }

    @Test
    void shouldMapUserToUserSummaryDto() {
        // given
        Instant deletedAt = Instant.parse("2026-09-01T07:00:00Z");

        User user = new User();
        user.setId(1L);
        user.setRole(UserRole.USER);
        user.setFirstName("Alex");
        user.setLastName("Smith");
        user.setPhone("+34123456789");
        user.setEmail("alex@test.com");
        user.setDeletedAt(deletedAt);

        // when
        UserSummaryDto result = userMapper.toUserSummaryDto(user);

        // then
        assertEquals(1L, result.getId());
        assertEquals(UserRole.USER, result.getRole());
        assertEquals("Alex", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("+34123456789", result.getPhone());
        assertEquals("alex@test.com", result.getEmail());
        assertEquals(deletedAt, result.getDeletedAt());
    }

    @Test
    void shouldMapUserCreateDtoToUserEntity() {
        // given
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setFirstName("Alex");
        userCreateDto.setLastName("Smith");
        userCreateDto.setPhone("+34123456789");
        userCreateDto.setEmail("alex@test.com");
        userCreateDto.setPassword("password123");
        userCreateDto.setInitialBalance(new BigDecimal("1500.00"));

        // when
        User result = userMapper.toEntity(userCreateDto);

        // then
        assertEquals(UserRole.USER, result.getRole());
        assertEquals("Alex", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("+34123456789", result.getPhone());
        assertEquals("alex@test.com", result.getEmail());
        assertEquals("password123", result.getPassword());
        assertEquals(new BigDecimal("1500.00"), result.getBalance());
    }

    @Test
    void shouldMapHotelAdminCreateDtoToUserEntity() {
        // given
        HotelAdminCreateDto hotelAdminCreateDto = new HotelAdminCreateDto();
        hotelAdminCreateDto.setFirstName("John");
        hotelAdminCreateDto.setLastName("Brown");
        hotelAdminCreateDto.setPhone("+34987654321");
        hotelAdminCreateDto.setEmail("admin@test.com");

        // when
        User result = userMapper.toEntity(hotelAdminCreateDto);

        // then
        assertEquals(UserRole.HOTEL_ADMIN, result.getRole());
        assertEquals("John", result.getFirstName());
        assertEquals("Brown", result.getLastName());
        assertEquals("+34987654321", result.getPhone());
        assertEquals("admin@test.com", result.getEmail());
        assertEquals(BigDecimal.ZERO, result.getBalance());
    }

    @Test
    void shouldUpdateUserEntity() {
        // given
        User user = new User();
        user.setFirstName("Old Name");
        user.setLastName("Old Last Name");
        user.setPhone("+34000000000");

        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setFirstName("New Name");
        userUpdateDto.setLastName("New Last Name");
        userUpdateDto.setPhone("+34111111111");

        // when
        userMapper.updateEntity(userUpdateDto, user);

        // then
        assertEquals("New Name", user.getFirstName());
        assertEquals("New Last Name", user.getLastName());
        assertEquals("+34111111111", user.getPhone());
    }

    @Test
    void shouldNotUpdateUserFieldsWhenValuesAreNull() {
        // given
        User user = new User();
        user.setFirstName("Alex");
        user.setLastName("Smith");
        user.setPhone("+34123456789");

        UserUpdateDto userUpdateDto = new UserUpdateDto();

        // when
        userMapper.updateEntity(userUpdateDto, user);

        // then
        assertEquals("Alex", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("+34123456789", user.getPhone());
    }

    @Test
    void shouldMapUsersToUserSummaryDtoList() {
        // given
        User firstUser = new User();
        firstUser.setId(1L);
        firstUser.setRole(UserRole.USER);
        firstUser.setFirstName("Alex");
        firstUser.setLastName("Smith");
        firstUser.setEmail("alex@test.com");

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setRole(UserRole.USER);
        secondUser.setFirstName("John");
        secondUser.setLastName("Brown");
        secondUser.setEmail("john@test.com");

        List<User> users = List.of(firstUser, secondUser);

        // when
        List<UserSummaryDto> result = userMapper.toUserSummaryDtoList(users);

        // then
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("Alex", result.get(0).getFirstName());
        assertEquals("alex@test.com", result.get(0).getEmail());

        assertEquals(2L, result.get(1).getId());
        assertEquals("John", result.get(1).getFirstName());
        assertEquals("john@test.com", result.get(1).getEmail());
    }

    @Test
    void shouldMapUserToHotelAdminDto() {
        // given
        City city = new City();
        city.setId(1L);
        city.setName("Madrid");

        Hotel hotel = new Hotel();
        hotel.setId(10L);
        hotel.setName("Central Hotel");
        hotel.setCity(city);
        hotel.setAddress("Main Street 10");

        User user = new User();
        user.setId(5L);
        user.setFirstName("John");
        user.setLastName("Brown");
        user.setPhone("+34987654321");
        user.setEmail("admin@test.com");
        user.setEmailVerified(true);
        user.setManagedHotels(Set.of(hotel));

        // when
        HotelAdminDto result = userMapper.toHotelAdminDto(user);

        // then
        assertEquals(5L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Brown", result.getLastName());
        assertEquals("+34987654321", result.getPhone());
        assertEquals("admin@test.com", result.getEmail());
        assertTrue(result.isEmailVerified());

        assertEquals(1, result.getManagedHotels().size());

        assertTrue(
                result.getManagedHotels().stream()
                        .anyMatch(managedHotel ->
                                managedHotel.getId().equals(10L)
                                        && managedHotel.getName().equals("Central Hotel"))
        );
    }
}