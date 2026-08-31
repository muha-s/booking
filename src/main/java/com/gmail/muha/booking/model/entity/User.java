package com.gmail.muha.booking.model.entity;

import com.gmail.muha.booking.model.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "pending_email", unique = true)
    private String pendingEmail;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal balance;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Booking> bookings = new ArrayList<>();

    @ManyToMany(mappedBy = "admins")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Hotel> managedHotels = new HashSet<>();

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;
}