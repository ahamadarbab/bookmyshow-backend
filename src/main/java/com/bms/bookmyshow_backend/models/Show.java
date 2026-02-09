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

    private String showName;

    private long startTimeInSec;

    @ManyToOne
    private Movie movie;

    private long endTimeInSec;

    private LocalDateTime displayStartTime;

    private LocalDateTime displayEndTime;

    private int totalTicketSold;

    private Double totalRevenue;

}
