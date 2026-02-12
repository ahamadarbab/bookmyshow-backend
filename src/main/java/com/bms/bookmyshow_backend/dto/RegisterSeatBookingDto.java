package com.bms.bookmyshow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterSeatBookingDto {
    private ArrayList<String> seatIds;
    private String paymentId;
    private String paymentSource;
}
