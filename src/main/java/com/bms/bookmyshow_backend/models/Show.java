package com.bms.bookmyshow_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shows")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Hall hall;

    @Column(nullable = false)
    private String showName;

    @Column(nullable = false)
    private long startTimeInSec;

    @ManyToOne
    private Movie movie;

    @Column(nullable = false)
    private long endTimeInSec;

    @Column(nullable = false)
    private LocalDateTime displayStartTime;

    @Column(nullable = false)
    private LocalDateTime displayEndTime;

    @Column(nullable = false)
    private int totalTicketSold;

    @Column(nullable = false)
    private Double totalRevenue;

}
