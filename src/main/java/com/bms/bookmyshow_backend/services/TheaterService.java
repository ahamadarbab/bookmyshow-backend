package com.bms.bookmyshow_backend.services;

import com.bms.bookmyshow_backend.dto.RegisterTheaterDto;
import com.bms.bookmyshow_backend.exception.UnAuthorizedException;
import com.bms.bookmyshow_backend.exception.UserNotFoundException;
import com.bms.bookmyshow_backend.models.Theater;
import com.bms.bookmyshow_backend.models.User;
import com.bms.bookmyshow_backend.repositories.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TheaterService {

    UserService userService;
    TheaterRepository theaterRepository;

    @Autowired
    public TheaterService(UserService userService, TheaterRepository theaterRepository) {
        this.userService = userService;
        this.theaterRepository = theaterRepository;
    }

    public Theater registerTheater(UUID userID, RegisterTheaterDto registerTheaterDto) {
        // 1. We want to validate whether userId exists in the user table or not.
        // 2. TheaterService -> UserService -> isUserIdExist()
        User theaterOwner = userService.isUserIdExists(userID);

        if(theaterOwner == null) {
            // If theaterOwner is null that means we got invalid id
            // We should throw the exception that user does not exist
            throw new UserNotFoundException("User does not exist!");
        }

        if(!theaterOwner.getUserType().equals("THEATER_OWNER")) {   // MOVIE_OWNER or CUSTOMER
            throw new UnAuthorizedException("User does not have permission to create theater");
        }

        Theater theater = new Theater();
        theater.setTheaterName(registerTheaterDto.getTheaterName());
        theater.setAddress(registerTheaterDto.getAddress());
        theater.setCity(registerTheaterDto.getCity());
        theater.setPincode(registerTheaterDto.getPincode());
        theater.setState(registerTheaterDto.getState());
        theater.setCountry(registerTheaterDto.getCountry());
        theater.setTheaterOwner((theaterOwner));

        return theaterRepository.save(theater);
    }
}
