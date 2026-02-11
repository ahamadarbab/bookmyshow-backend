package com.bms.bookmyshow_backend.repositories;

import com.bms.bookmyshow_backend.models.Hall;
import com.bms.bookmyshow_backend.models.HallRowMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HallRowMappingRepository extends JpaRepository<HallRowMapping, UUID> {

    public List<HallRowMapping> findRowMappingByHall(Hall hall);
}
