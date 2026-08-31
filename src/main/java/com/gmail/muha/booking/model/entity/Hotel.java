package com.gmail.muha.booking.model.entity;

import com.gmail.muha.booking.model.entity.enums.NumberOfStars;
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
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private City city;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "number_of_stars", nullable = false)
    private NumberOfStars numberOfStars;

    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(name = "base_price_per_night", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePricePerNight;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "hotel")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Room> rooms = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "hotels_admins",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> admins = new HashSet<>();
}