package com.bms.bookmyshow_backend.repositories;

import com.bms.bookmyshow_backend.models.ShowPriceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShowPriceMappingRepository extends JpaRepository<ShowPriceMapping, UUID> {

    @Query(value = "select * from showpricemappings where show_id=:showId and hall_row_mapping_id=:hallRowMappingId", nativeQuery = true)
    public ShowPriceMapping getPriceMappingRecordByShowAndRowMapping(UUID showId, UUID hallRowMappingId);
}
