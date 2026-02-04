package com.bms.bookmyshow_backend.controllers;

import com.bms.bookmyshow_backend.dto.RegisterTheaterDto;
import com.bms.bookmyshow_backend.exception.UnAuthorizedException;
import com.bms.bookmyshow_backend.exception.UserNotFoundException;
import com.bms.bookmyshow_backend.models.Theater;
import com.bms.bookmyshow_backend.services.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/theater")
public class TheaterController {

    TheaterService theaterService;

    @Autowired
    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @PostMapping("/register")
    public ResponseEntity registerTheater(@RequestParam UUID userId, @RequestBody RegisterTheaterDto registerTheaterDto) {
        try {
            Theater theater = theaterService.registerTheater(userId, registerTheaterDto);
            return new ResponseEntity(theater, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);    //4XX
        } catch (UnAuthorizedException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);   //401
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
