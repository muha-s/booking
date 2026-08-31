package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query("""
            select user
            from User user
            where user.id = :id
              and user.deletedAt is null
            """)
    Optional<User> findActiveById(Long id);

    @Query("""
            select user
            from User user
            where user.email = :email
              and user.deletedAt is null
            """)
    Optional<User> findActiveByEmail(@Param("email") String email);

    @Query("""
            select user
            from User user
            where user.role = :role
              and user.deletedAt is null
            """)
    List<User> findAllActiveByRole(@Param("role") UserRole role);

    @Query("""
            select user
            from User user
            where lower(user.email) = lower(:email)
              and user.deletedAt is not null
            """)
    Optional<User> findDeletedByEmail(@Param("email") String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPendingEmailIgnoreCase(String pendingEmail);

    List<User> findAllByRoleOrderByIdAsc(UserRole role);
}
