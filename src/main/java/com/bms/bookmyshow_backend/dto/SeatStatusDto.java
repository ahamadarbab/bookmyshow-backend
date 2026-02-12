package com.bms.bookmyshow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeatStatusDto {
    private UUID showId;
    private UUID hallId;
    private String seatId;  // A-10
    private String seatType;
    private boolean status;
    private Double price;
}
