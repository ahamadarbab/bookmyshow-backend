package com.bms.bookmyshow_backend.repositories;

import com.bms.bookmyshow_backend.models.ShowPriceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShowPriceMappingRepository extends JpaRepository<ShowPriceMapping, UUID> {
}
