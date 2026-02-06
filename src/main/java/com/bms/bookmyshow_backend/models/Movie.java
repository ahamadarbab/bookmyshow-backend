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

    @ManyToOne
    private User movieOwner;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private Double totalIncome;

    @Column(nullable = false)
    private int totalTicketSold;

    @Column(nullable = false)
    private LocalDate launchDate;

    @Column(nullable = false)
    private Double movieDurationInHours;

    @Column(nullable = false)
    private String genre;

    @ManyToMany
    private List<Artist> artists;

    @ManyToOne
    private Artist director;
}
