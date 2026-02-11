package com.bms.bookmyshow_backend.services;

import com.bms.bookmyshow_backend.models.SeatBooking;
import com.bms.bookmyshow_backend.repositories.SeatBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SeatBookingService {

    SeatBookingRepository seatBookingRepository;

    @Autowired
    public SeatBookingService(SeatBookingRepository seatBookingRepository) {
        this.seatBookingRepository = seatBookingRepository;
    }

    public boolean isSeatAvailableForShow(String seatId, UUID showId) {
        SeatBooking booking = seatBookingRepository.getSeatBookingBySeatIdAndShowId(seatId, showId);
        if(booking == null) {
            return true;
        }
        return false;
    }
}
