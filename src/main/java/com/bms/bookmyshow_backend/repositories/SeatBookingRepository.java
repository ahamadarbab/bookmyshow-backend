package com.bms.bookmyshow_backend.repositories;

import com.bms.bookmyshow_backend.models.SeatBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeatBookingRepository extends JpaRepository<SeatBooking, UUID> {

    @Query(value = "select * from seatbookings where show_id=:showId and seat_id=:seatId", nativeQuery = true)
    public SeatBooking getSeatBookingBySeatIdAndShowId(String seatId, UUID showId);

}
