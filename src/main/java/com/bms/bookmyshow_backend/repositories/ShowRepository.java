package com.bms.bookmyshow_backend.repositories;

import com.bms.bookmyshow_backend.models.Hall;
import com.bms.bookmyshow_backend.models.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShowRepository extends JpaRepository<Show, UUID> {

    public List<Show> findByHall(Hall hall);
}
