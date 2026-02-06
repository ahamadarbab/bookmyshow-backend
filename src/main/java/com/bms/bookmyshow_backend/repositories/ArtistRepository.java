package com.bms.bookmyshow_backend.repositories;

import com.bms.bookmyshow_backend.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID> {

    // Select * from artist where name = 'xyz'
    public Artist findByArtistName(String artistName);
}
