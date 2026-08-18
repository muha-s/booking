package com.gmail.muha.booking.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "city")
    private List<Hotel> hotels = new ArrayList<>();

}