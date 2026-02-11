package com.bms.bookmyshow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeatStatusDto {
    UUID showId;
    UUID hallId;
    String seatId;  // A-10
    String seatType;
    boolean status;
    Double price;
}
