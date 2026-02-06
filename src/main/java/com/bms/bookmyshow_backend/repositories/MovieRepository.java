package com.bms.bookmyshow_backend.repositories;

import com.bms.bookmyshow_backend.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
}
