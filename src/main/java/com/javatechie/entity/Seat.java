package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String movieName;

    private boolean booked;

    // Optimistic Locking column
    @Version
    private int version;

    // Getters and Setters
}
