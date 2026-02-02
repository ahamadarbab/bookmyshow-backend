package com.bms.bookmyshow_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "showpricemappings")
public class ShowPriceMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Show show;

    @ManyToOne
    private Hall hall;

    @ManyToOne
    private HallRowMapping hallRowMapping;

    @Column(nullable = false)
    private Double price;
}
