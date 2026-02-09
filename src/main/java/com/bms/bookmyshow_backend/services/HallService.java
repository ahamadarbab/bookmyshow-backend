package com.bms.bookmyshow_backend.services;

import com.bms.bookmyshow_backend.dto.RegisterHallDto;
import com.bms.bookmyshow_backend.exception.TheaterNotFoundException;
import com.bms.bookmyshow_backend.exception.UnAuthorizedException;
import com.bms.bookmyshow_backend.exception.UserNotFoundException;
import com.bms.bookmyshow_backend.models.Hall;
import com.bms.bookmyshow_backend.models.Theater;
import com.bms.bookmyshow_backend.models.User;
import com.bms.bookmyshow_backend.repositories.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class HallService {

    UserService userService;
    TheaterService theaterService;
    HallRepository hallRepository;

    @Autowired
    public HallService(UserService userService, TheaterService theaterService, HallRepository hallRepository) {
        this.userService = userService;
        this.theaterService = theaterService;
        this.hallRepository = hallRepository;
    }

    public Hall registerHall(RegisterHallDto registerHallDto, UUID userId, UUID theaterId) {
        // 1. We want to validate whether userId exists in the user table or not.
        // 2. HallService -> UserService -> isUserIdExist()
        User theaterOwner = userService.isUserIdExists(userId);

        if(theaterOwner == null) {
            // If theaterOwner is null that means we got invalid id
            // We should throw the exception that user does not exist
            throw new UserNotFoundException("User does not exist!");
        }

        if(!theaterOwner.getUserType().equals("THEATER_OWNER")) {   // MOVIE_OWNER or CUSTOMER
            throw new UnAuthorizedException("User does not have permission to create hall");
        }

        Theater theater = theaterService.isTheaterExists(theaterId);

        if(theater == null) {
            throw new TheaterNotFoundException("Theater does not exists");
        }

        Hall hall = new Hall();
        hall.setHallName(registerHallDto.getHallName());
        hall.setCapacity(registerHallDto.getCapacity());
        hall.setTheater(theater);

        return hallRepository.save(hall);
    }

    public Hall isHallIdValid(UUID hallId) {
        Optional<Hall> hall = hallRepository.findById(hallId);
        return hall.orElse(null);
    }
}
