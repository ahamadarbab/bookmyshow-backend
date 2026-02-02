package com.bms.bookmyshow_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String movieName;

    @Column(nullable = false)
    private String movieOwner;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private double totalIncome;

    @Column(nullable = false)
    private int totalTicketSold;

    @Column(nullable = false)
    private LocalDate launchDate;

    @Column(nullable = false)
    private Double movieDurationInHours;

    @Column(nullable = false)
    private String Genre;

    @ManyToMany
    private List<Artist> artists;

    @ManyToOne
    private Artist director;
}
