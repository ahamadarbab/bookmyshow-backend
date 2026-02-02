package com.bms.bookmyshow_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private User customer;

    @ManyToOne
    private Theater theater;

    private List<SeatBooking> seatBookings;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private String paymentSource;

    @Column(nullable = false)
    private String paymentId;
}
