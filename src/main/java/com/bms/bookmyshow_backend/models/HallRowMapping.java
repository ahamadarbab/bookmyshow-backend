package com.bms.bookmyshow_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "hallrowmappings")
public class HallRowMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Hall hall;

    @Column(nullable = false)
    private String rowType;

    @Column(nullable = false)
    private String rowRange;

    @Column(nullable = false)
    private int seatCount;

}
