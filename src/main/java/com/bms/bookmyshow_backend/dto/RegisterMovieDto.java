package com.bms.bookmyshow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMovieDto {

    private String movieName;
    private UUID movieOwnerUserId;
    private LocalDate launchDate;
    private Double movieDurationInHours;
    private String genre;
    private List<String> artists;
    private String director;

}
