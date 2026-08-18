package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
           select user
           from User user
           where user.deletedAt is null
           """)
    List<User> findAllActive();

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
}
